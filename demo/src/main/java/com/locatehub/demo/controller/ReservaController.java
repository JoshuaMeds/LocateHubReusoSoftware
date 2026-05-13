package com.locatehub.demo.controller;

import com.locatehub.demo.model.Ativo;
import com.locatehub.demo.model.Reserva;
import com.locatehub.demo.model.User;
import com.locatehub.demo.model.UserLocador;
import com.locatehub.demo.model.UserLocatario;
import com.locatehub.demo.repository.AtivoRepository;
import com.locatehub.demo.repository.ReservaRepository;
import com.locatehub.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaRepository reservaRepository;
    private final AtivoRepository ativoRepository;
    private final UserService userService;

    public ReservaController(
            ReservaRepository reservaRepository,
            AtivoRepository ativoRepository,
            UserService userService
    ) {
        this.reservaRepository = reservaRepository;
        this.ativoRepository = ativoRepository;
        this.userService = userService;
    }

    @GetMapping("/minhas")
    public ResponseEntity<?> minhasReservas(HttpSession session) {
        Optional<UserLocatario> locatario = locatarioLogado(session);
        if (locatario.isEmpty()) {
            return ResponseEntity.status(403).body("Apenas locatarios podem acessar suas reservas.");
        }

        List<ReservaResponse> reservas = reservaRepository.findByLocatario_IdOrderByDataInicioDesc(locatario.get().getId())
                .stream()
                .map(ReservaResponse::from)
                .toList();

        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/ativos/{ativoId}")
    public ResponseEntity<?> reservasDoAtivo(@PathVariable Long ativoId, HttpSession session) {
        Optional<User> usuario = usuarioLogado(session);
        if (usuario.isEmpty() || !(usuario.get() instanceof UserLocador)) {
            return ResponseEntity.status(403).body("Apenas locadores podem ver reservas do ativo.");
        }

        Optional<Ativo> ativo = ativoRepository.findById(ativoId);
        if (ativo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!ativo.get().podeSerGerenciadoPor(usuario.get().getId())) {
            return ResponseEntity.status(403).body("Voce so pode ver reservas dos seus ativos.");
        }

        List<ReservaResponse> reservas = reservaRepository.findByAtivo_IdOrderByDataInicioAsc(ativoId)
                .stream()
                .map(ReservaResponse::from)
                .toList();

        return ResponseEntity.ok(reservas);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> reservar(@RequestBody ReservaRequest request, HttpSession session) {
        Optional<UserLocatario> locatario = locatarioLogado(session);
        if (locatario.isEmpty()) {
            return ResponseEntity.status(403).body("Apenas locatarios podem criar reservas.");
        }

        if (request.ativoId() == null || request.dataInicio() == null || request.dataFim() == null) {
            return ResponseEntity.badRequest().body("Ativo, data inicial e data final sao obrigatorios.");
        }

        Optional<Ativo> ativoOptional = ativoRepository.findById(request.ativoId());
        if (ativoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ativo ativo = ativoOptional.get();
        if (!ativo.isDisponivel()) {
            return ResponseEntity.badRequest().body("Este ativo esta indisponivel para reserva.");
        }

        long dias = ChronoUnit.DAYS.between(request.dataInicio(), request.dataFim());
        if (dias <= 0) {
            return ResponseEntity.badRequest().body("A data final deve ser posterior a data inicial.");
        }

        if (request.dataInicio().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body("A data inicial nao pode estar no passado.");
        }

        if (!reservaRepository.findConflitos(ativo.getId(), request.dataInicio(), request.dataFim()).isEmpty()) {
            return ResponseEntity.badRequest().body("Ja existe reserva para este ativo no periodo informado.");
        }

        try {
            BigDecimal valorTotal = ativo.calcularValorLocacao((int) dias);
            Reserva reserva = reservaRepository.save(new Reserva(
                    ativo,
                    locatario.get(),
                    request.dataInicio(),
                    request.dataFim(),
                    valorTotal
            ));

            return ResponseEntity.ok(ReservaResponse.from(reserva));
        } catch (IllegalStateException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id, HttpSession session) {
        Optional<UserLocatario> locatario = locatarioLogado(session);
        if (locatario.isEmpty()) {
            return ResponseEntity.status(403).body("Apenas locatarios podem cancelar reservas.");
        }

        Optional<Reserva> reservaOptional = reservaRepository.findById(id);
        if (reservaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reserva reserva = reservaOptional.get();
        if (!reserva.getLocatario().getId().equals(locatario.get().getId())) {
            return ResponseEntity.status(403).body("Voce so pode cancelar suas proprias reservas.");
        }

        if (!reserva.podeCancelar(LocalDate.now())) {
            return ResponseEntity.badRequest().body("A reserva so pode ser cancelada antes da data inicial.");
        }

        reserva.cancelar();
        reservaRepository.save(reserva);

        return ResponseEntity.noContent().build();
    }

    private Optional<UserLocatario> locatarioLogado(HttpSession session) {
        return usuarioLogado(session)
                .filter(UserLocatario.class::isInstance)
                .map(UserLocatario.class::cast);
    }

    private Optional<User> usuarioLogado(HttpSession session) {
        Object email = session.getAttribute("user");
        if (!(email instanceof String emailUsuario)) {
            return Optional.empty();
        }

        return userService.findByEmail(emailUsuario);
    }

    public record ReservaRequest(Long ativoId, LocalDate dataInicio, LocalDate dataFim) {
    }

    public record ReservaResponse(
            Long id,
            Long ativoId,
            String produto,
            String locatario,
            LocalDate dataInicio,
            LocalDate dataFim,
            BigDecimal valorTotal,
            boolean cancelada,
            String status,
            boolean podeCancelar
    ) {
        static ReservaResponse from(Reserva reserva) {
            LocalDate hoje = LocalDate.now();
            return new ReservaResponse(
                    reserva.getId(),
                    reserva.getAtivo().getId(),
                    reserva.getAtivo().getTitulo(),
                    reserva.getLocatario().getNome(),
                    reserva.getDataInicio(),
                    reserva.getDataFim(),
                    reserva.getValorTotal(),
                    reserva.isCancelada(),
                    status(reserva, hoje),
                    reserva.podeCancelar(hoje)
            );
        }

        private static String status(Reserva reserva, LocalDate hoje) {
            if (reserva.isCancelada()) {
                return "Cancelada";
            }
            if (hoje.isBefore(reserva.getDataInicio())) {
                return "Futura";
            }
            if (hoje.isBefore(reserva.getDataFim())) {
                return "Em andamento";
            }
            return "Finalizada";
        }
    }
}

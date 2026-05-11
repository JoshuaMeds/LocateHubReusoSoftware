package com.locatehub.demo.controller;

import com.locatehub.demo.dto.AtivoResumoResponse;
import com.locatehub.demo.model.Ativo;
import com.locatehub.demo.model.User;
import com.locatehub.demo.model.UserLocador;
import com.locatehub.demo.model.UserLocatario;
import com.locatehub.demo.repository.AtivoRepository;
import com.locatehub.demo.repository.ListaDesejosRepository;
import com.locatehub.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/ativos")
public class AtivoController {

    private final AtivoRepository ativoRepository;
    private final ListaDesejosRepository listaDesejosRepository;
    private final UserService userService;

    public AtivoController(
            AtivoRepository ativoRepository,
            ListaDesejosRepository listaDesejosRepository,
            UserService userService
    ) {
        this.ativoRepository = ativoRepository;
        this.listaDesejosRepository = listaDesejosRepository;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AtivoResumoResponse>> listar(
            @RequestParam(required = false) String tipo,
            HttpSession session
    ) {
        String tipoNormalizado = tipo == null ? null : tipo.toUpperCase(Locale.ROOT);
        Optional<Long> locatarioId = usuarioLogado(session)
                .filter(UserLocatario.class::isInstance)
                .map(User::getId);

        List<AtivoResumoResponse> ativos = ativoRepository.findAll()
                .stream()
                .filter(ativo -> tipoNormalizado == null || AtivoResumoResponse.tipoCodigo(ativo).equals(tipoNormalizado))
                .map(ativo -> AtivoResumoResponse.from(
                        ativo,
                        locatarioId
                                .map(id -> listaDesejosRepository.existsByLocatario_IdAndAtivo_Id(id, ativo.getId()))
                                .orElse(false)
                ))
                .toList();

        return ResponseEntity.ok(ativos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id, HttpSession session) {
        Optional<Ativo> ativoOptional = ativoRepository.findById(id);

        if (ativoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ativo ativo = ativoOptional.get();
        Optional<User> usuario = usuarioLogado(session);
        boolean naListaDesejos = usuario
                .filter(UserLocatario.class::isInstance)
                .map(User::getId)
                .map(usuarioId -> listaDesejosRepository.existsByLocatario_IdAndAtivo_Id(usuarioId, ativo.getId()))
                .orElse(false);

        boolean podeGerenciar = usuario
                .filter(UserLocador.class::isInstance)
                .map(User::getId)
                .map(ativo::podeSerGerenciadoPor)
                .orElse(false);
        boolean podeUsarListaDesejos = usuario
                .filter(UserLocatario.class::isInstance)
                .isPresent();

        return ResponseEntity.ok(AtivoDetalheResponse.from(ativo, naListaDesejos, podeGerenciar, podeUsarListaDesejos));
    }

    @PostMapping("/{id}/simular")
    public ResponseEntity<?> simularReserva(@PathVariable Long id, @RequestBody SimulacaoRequest request) {
        Optional<Ativo> ativoOptional = ativoRepository.findById(id);

        if (ativoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        long dias = ChronoUnit.DAYS.between(request.dataInicio(), request.dataFim());
        if (dias <= 0) {
            return ResponseEntity.badRequest().body("A data final deve ser posterior à data inicial.");
        }

        try {
            Ativo ativo = ativoOptional.get();
            BigDecimal valorTotal = ativo.calcularValorLocacao((int) dias);
            return ResponseEntity.ok(new SimulacaoResponse((int) dias, ativo.getValorDiaria(), valorTotal));
        } catch (IllegalStateException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<?> alterarDisponibilidade(
            @PathVariable Long id,
            @RequestBody DisponibilidadeRequest request,
            HttpSession session
    ) {
        Optional<User> usuario = usuarioLogado(session);
        if (usuario.isEmpty() || !(usuario.get() instanceof UserLocador)) {
            return ResponseEntity.status(403).body("Apenas locadores podem alterar disponibilidade.");
        }

        Optional<Ativo> ativoOptional = ativoRepository.findById(id);
        if (ativoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ativo ativo = ativoOptional.get();
        if (!ativo.podeSerGerenciadoPor(usuario.get().getId())) {
            return ResponseEntity.status(403).body("Você só pode alterar ativos criados por você.");
        }

        ativo.setDisponivel(request.disponivel());
        ativoRepository.save(ativo);

        return ResponseEntity.ok(AtivoDetalheResponse.from(ativo, false, true, false));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id, HttpSession session) {
        Optional<User> usuario = usuarioLogado(session);
        if (usuario.isEmpty() || !(usuario.get() instanceof UserLocador)) {
            return ResponseEntity.status(403).body("Apenas locadores podem excluir ativos.");
        }

        Optional<Ativo> ativoOptional = ativoRepository.findById(id);
        if (ativoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ativo ativo = ativoOptional.get();
        if (!ativo.podeSerGerenciadoPor(usuario.get().getId())) {
            return ResponseEntity.status(403).body("Você só pode excluir ativos criados por você.");
        }

        ativoRepository.delete(ativo);
        return ResponseEntity.noContent().build();
    }

    private Optional<User> usuarioLogado(HttpSession session) {
        Object email = session.getAttribute("user");
        if (!(email instanceof String emailUsuario)) {
            return Optional.empty();
        }

        return userService.findByEmail(emailUsuario);
    }

    public record SimulacaoRequest(LocalDate dataInicio, LocalDate dataFim) {
    }

    public record SimulacaoResponse(int dias, BigDecimal valorDiaria, BigDecimal valorTotal) {
    }

    public record DisponibilidadeRequest(boolean disponivel) {
    }

    public record AtivoDetalheResponse(
            Long id,
            String titulo,
            String descricao,
            BigDecimal valorDiaria,
            boolean disponivel,
            String tipo,
            String tipoNome,
            Long donoId,
            boolean naListaDesejos,
            boolean podeGerenciar,
            boolean podeUsarListaDesejos
    ) {
        static AtivoDetalheResponse from(
                Ativo ativo,
                boolean naListaDesejos,
                boolean podeGerenciar,
                boolean podeUsarListaDesejos
        ) {
            return new AtivoDetalheResponse(
                    ativo.getId(),
                    ativo.getTitulo(),
                    ativo.getDescricao(),
                    ativo.getValorDiaria(),
                    ativo.isDisponivel(),
                    AtivoResumoResponse.tipoCodigo(ativo),
                    ativo.getTipo(),
                    ativo.getDonoId(),
                    naListaDesejos,
                    podeGerenciar,
                    podeUsarListaDesejos
            );
        }
    }
}

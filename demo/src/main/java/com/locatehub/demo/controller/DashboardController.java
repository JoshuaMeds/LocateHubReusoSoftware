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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final ReservaRepository reservaRepository;
    private final AtivoRepository ativoRepository;
    private final UserService userService;

    public DashboardController(ReservaRepository reservaRepository,
                               AtivoRepository ativoRepository,
                               UserService userService) {
        this.reservaRepository = reservaRepository;
        this.ativoRepository = ativoRepository;
        this.userService = userService;
    }

    @GetMapping("/locatario")
    public ResponseEntity<?> dashboardLocatario(HttpSession session) {
        Optional<UserLocatario> locatario = locatarioLogado(session);
        if (locatario.isEmpty())
            return ResponseEntity.status(403).body("Apenas locatarios podem acessar este dashboard.");

        Long id = locatario.get().getId();
        LocalDate hoje = LocalDate.now();
        YearMonth mesAtual = YearMonth.from(hoje);

        BigDecimal totalGasto = reservaRepository.somarGastoTotalLocatario(id);
        BigDecimal gastoMes = reservaRepository.somarGastoNoPeriodoLocatario(
                id, mesAtual.atDay(1), mesAtual.atEndOfMonth()
        );

        List<Reserva> reservas = reservaRepository.findByLocatario_IdOrderByDataInicioDesc(id);
        long total = reservas.size();
        long emAndamento = reservas.stream()
                .filter(r -> !r.isCancelada()
                        && !hoje.isBefore(r.getDataInicio())
                        && hoje.isBefore(r.getDataFim()))
                .count();
        long finalizadas = reservas.stream()
                .filter(r -> !r.isCancelada() && !hoje.isBefore(r.getDataFim()))
                .count();
        long canceladas = reservas.stream().filter(Reserva::isCancelada).count();

        return ResponseEntity.ok(new DashboardLocatarioResponse(
                locatario.get().getNome(),
                locatario.get().getEmail(),
                totalGasto,
                gastoMes,
                total,
                emAndamento,
                finalizadas,
                canceladas
        ));
    }

    @GetMapping("/locador")
    public ResponseEntity<?> dashboardLocador(HttpSession session) {
        Optional<User> usuario = usuarioLogado(session);
        if (usuario.isEmpty() || !(usuario.get() instanceof UserLocador locador))
            return ResponseEntity.status(403).body("Apenas locadores podem acessar este dashboard.");

        Long id = locador.getId();
        LocalDate hoje = LocalDate.now();

        BigDecimal receitaRealizada = reservaRepository.somarReceitaRealizadaLocador(id, hoje);
        BigDecimal receitaProjetada = reservaRepository.somarReceitaProjetadaLocador(id, hoje);

        List<Ativo> meusAtivos = ativoRepository.findByDonoId(id);
        long totalAtivos = meusAtivos.size();
        long ativosDisponiveis = meusAtivos.stream().filter(Ativo::isDisponivel).count();

        List<Reserva> minhasReservas = reservaRepository.findByLocadorId(id);
        long totalReservasRecebidas = minhasReservas.size();
        long reservasAtivas = minhasReservas.stream()
                .filter(r -> !r.isCancelada()
                        && !hoje.isBefore(r.getDataInicio())
                        && hoje.isBefore(r.getDataFim()))
                .count();

        return ResponseEntity.ok(new DashboardLocadorResponse(
                locador.getNome(),
                locador.getEmail(),
                receitaRealizada,
                receitaProjetada,
                totalAtivos,
                ativosDisponiveis,
                totalReservasRecebidas,
                reservasAtivas
        ));
    }

    private Optional<UserLocatario> locatarioLogado(HttpSession session) {
        return usuarioLogado(session)
                .filter(UserLocatario.class::isInstance)
                .map(UserLocatario.class::cast);
    }

    private Optional<User> usuarioLogado(HttpSession session) {
        Object email = session.getAttribute("user");
        if (!(email instanceof String emailUsuario)) return Optional.empty();
        return userService.findByEmail(emailUsuario);
    }

    public record DashboardLocatarioResponse(
            String nome,
            String email,
            BigDecimal totalGasto,
            BigDecimal gastoMesAtual,
            long totalReservas,
            long reservasEmAndamento,
            long reservasFinalizadas,
            long reservasCanceladas
    ) {}

    public record DashboardLocadorResponse(
            String nome,
            String email,
            BigDecimal receitaRealizada,
            BigDecimal receitaProjetada,
            long totalAtivosCadastrados,
            long ativosDisponiveis,
            long totalReservasRecebidas,
            long reservasEmAndamento
    ) {}
}

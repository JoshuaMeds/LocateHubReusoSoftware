package com.locatehub.demo.controller;

import com.locatehub.demo.dto.AtivoResumoResponse;
import com.locatehub.demo.model.Ativo;
import com.locatehub.demo.model.ListaDesejos;
import com.locatehub.demo.model.User;
import com.locatehub.demo.model.UserLocatario;
import com.locatehub.demo.repository.AtivoRepository;
import com.locatehub.demo.repository.ListaDesejosRepository;
import com.locatehub.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/lista-desejos")
public class ListaDesejosController {

    private final ListaDesejosRepository listaDesejosRepository;
    private final AtivoRepository ativoRepository;
    private final UserService userService;

    public ListaDesejosController(
            ListaDesejosRepository listaDesejosRepository,
            AtivoRepository ativoRepository,
            UserService userService
    ) {
        this.listaDesejosRepository = listaDesejosRepository;
        this.ativoRepository = ativoRepository;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) String tipo, HttpSession session) {
        Optional<UserLocatario> locatario = locatarioLogado(session);
        if (locatario.isEmpty()) {
            return ResponseEntity.status(403).body("Apenas locatários podem acessar a lista de desejos.");
        }

        String tipoNormalizado = tipo == null ? null : tipo.toUpperCase(Locale.ROOT);
        List<AtivoResumoResponse> ativos = listaDesejosRepository.findByLocatario_Id(locatario.get().getId())
                .stream()
                .map(ListaDesejos::getAtivo)
                .filter(ativo -> tipoNormalizado == null || AtivoResumoResponse.tipoCodigo(ativo).equals(tipoNormalizado))
                .map(ativo -> AtivoResumoResponse.from(ativo, true))
                .toList();

        return ResponseEntity.ok(ativos);
    }

    @PostMapping("/{ativoId}")
    public ResponseEntity<?> adicionar(@PathVariable Long ativoId, HttpSession session) {
        Optional<UserLocatario> locatario = locatarioLogado(session);
        if (locatario.isEmpty()) {
            return ResponseEntity.status(403).body("Apenas locatários podem adicionar à lista de desejos.");
        }

        Optional<Ativo> ativo = ativoRepository.findById(ativoId);
        if (ativo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        boolean jaExiste = listaDesejosRepository.existsByLocatario_IdAndAtivo_Id(locatario.get().getId(), ativoId);
        if (!jaExiste) {
            listaDesejosRepository.save(new ListaDesejos(locatario.get(), ativo.get()));
        }

        return ResponseEntity.ok(AtivoResumoResponse.from(ativo.get(), true));
    }

    @DeleteMapping("/{ativoId}")
    public ResponseEntity<?> remover(@PathVariable Long ativoId, HttpSession session) {
        Optional<UserLocatario> locatario = locatarioLogado(session);
        if (locatario.isEmpty()) {
            return ResponseEntity.status(403).body("Apenas locatários podem remover da lista de desejos.");
        }

        listaDesejosRepository.findByLocatario_IdAndAtivo_Id(locatario.get().getId(), ativoId)
                .ifPresent(listaDesejosRepository::delete);

        return ResponseEntity.noContent().build();
    }

    private Optional<UserLocatario> locatarioLogado(HttpSession session) {
        Object email = session.getAttribute("user");
        if (!(email instanceof String emailUsuario)) {
            return Optional.empty();
        }

        return userService.findByEmail(emailUsuario)
                .filter(UserLocatario.class::isInstance)
                .map(UserLocatario.class::cast);
    }

}

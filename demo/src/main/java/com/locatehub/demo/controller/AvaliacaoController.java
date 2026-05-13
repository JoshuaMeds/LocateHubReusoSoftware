package com.locatehub.demo.controller;

import com.locatehub.demo.model.Avaliacao;
import com.locatehub.demo.model.UserLocatario;
import com.locatehub.demo.service.AvaliacaoService;
import com.locatehub.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;
    private final UserService userService;

    public AvaliacaoController(AvaliacaoService avaliacaoService, UserService userService) {
        this.avaliacaoService = avaliacaoService;
        this.userService = userService;
    }

    @PostMapping("/{reservaId}")
    public ResponseEntity<?> avaliar(@PathVariable Long reservaId, @RequestBody AvaliacaoRequest request, HttpSession session) {
        Optional<UserLocatario> locatario = locatarioLogado(session);
        
        if (locatario.isEmpty()) {
            return ResponseEntity.status(403).body("Apenas locatários logados podem avaliar.");
        }

        try {
            Avaliacao avaliacao = avaliacaoService.avaliarReserva(reservaId, request.nota(), request.comentario());
            return ResponseEntity.ok("Avaliação salva com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Optional<UserLocatario> locatarioLogado(HttpSession session) {
        Object email = session.getAttribute("user");
        if (!(email instanceof String emailUsuario)) return Optional.empty();
        return userService.findByEmail(emailUsuario).filter(UserLocatario.class::isInstance).map(UserLocatario.class::cast);
    }

    public record AvaliacaoRequest(Integer nota, String comentario) {}
}
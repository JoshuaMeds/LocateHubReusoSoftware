package com.locatehub.demo.controller;

import com.locatehub.demo.dto.LoginRequest;
import com.locatehub.demo.model.User;
import com.locatehub.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LoginController {

    private final UserService service;

    public LoginController(UserService service) {
        this.service = service;
    }

    @GetMapping("/session")
    public ResponseEntity<String> getSession(HttpSession session){
        String user = (String) session.getAttribute("user");
        if(user != null){
            return ResponseEntity.ok("");
        }

        return ResponseEntity.status(403).body("");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(

            @RequestBody LoginRequest request,
            HttpSession session
    ) {
        Optional<User> user = service.findByEmail(request.email());

        if (user.isPresent() && user.get().getSenha().equals(request.senha())){
            session.setAttribute("user", request.email());
            return ResponseEntity.ok("Login feito com sucesso");
        }

        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }
}

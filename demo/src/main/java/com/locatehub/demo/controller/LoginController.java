package com.locatehub.demo.controller;

import com.locatehub.demo.dto.LoginRequest;
import com.locatehub.demo.dto.RegisterRequest;
import com.locatehub.demo.model.User;
import com.locatehub.demo.model.UserLocador;
import com.locatehub.demo.model.UserLocatario;
import com.locatehub.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<String> getSession(HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if(session == null){
            return ResponseEntity.status(403).body("No session");
        }

        System.out.println("SESSION ID /session: " + session.getId());

        String user = (String) session.getAttribute("user");

        System.out.println("USER /session: " + user);


        if(user != null){
            Optional<User> userLogado = service.findByEmail(user);
            String id = userLogado.get().getId().toString();
            return ResponseEntity.ok(id);
        }

        return ResponseEntity.status(403).body("Unauthorized");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request,
            HttpSession session
    ) {

        System.out.println("SESSION ID /login: " + session.getId());

        Optional<User> user = service.findByEmail(request.email());

        if (user.isPresent() && user.get().getSenha().equals(request.senha())){
            session.setAttribute("user", request.email());

            System.out.println("USER STORED: " + request.email());

            return ResponseEntity.ok("Login feito com sucesso");
        }

        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request,
            HttpSession session
    ) {
        System.out.println("oi");
        Optional<User> userEmail = service.findByEmail(request.email());
        if(userEmail.isPresent()){
            return ResponseEntity.status(401).body("Email já cadastrado");
        }
        Optional<User> userDocumento = service.findByDocumento(request.documento());
        if(userDocumento.isPresent()){
            return ResponseEntity.status(401).body("Documento já cadastrado");
        }

        if(request.locador()){
            service.save(
                    new UserLocador(
                            request.nome(),
                            request.documento(),
                            request.senha(),
                            request.email()
                    ));
        }
        else{
            service.save(
                    new UserLocatario(
                            request.nome(),
                            request.email(),
                            request.documento(),
                            request.senha()
                    )
            );
        }
        return ResponseEntity.ok("Ok");
    }
}

package com.locatehub.demo.config;

import com.locatehub.demo.model.User;
import com.locatehub.demo.model.UserLocador;
import com.locatehub.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository repository) {
        return args -> {

            if (repository.count() == 0) {

                User user = new UserLocador(); // ou Admin
                user.setEmail("user@gmail.com");
                user.setNome("user");
                user.setDocumento("Doc");
                user.setSenha("user");
                repository.save(user);

                System.out.println("Usuário inicial criado!");
            }
        };
    }
}

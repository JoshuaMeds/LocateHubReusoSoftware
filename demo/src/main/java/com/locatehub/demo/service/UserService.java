package com.locatehub.demo.service;

import com.locatehub.demo.model.User;
import com.locatehub.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User save(User user) {
        return repository.save(user);
    }

    public List<User> findAll(){
        return repository.findAll();
    }

    public Optional<User> findByEmail(String email){
        return repository.findByEmail(email);
    }
}
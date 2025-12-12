package com.diberardino.authservice.service;

import com.diberardino.authservice.model.User;
import com.diberardino.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public boolean usernameExists(String u) {
        return repo.existsByUsername(u);
    }

    public boolean emailExists(String e) {
        return repo.existsByEmail(e);
    }

    public User register(String username, String email, String password) {
        User u = new User(username, email, encoder.encode(password));
        return repo.save(u);
    }

    public User getByUsername(String u) {
        return repo.findByUsername(u).orElse(null);
    }
}

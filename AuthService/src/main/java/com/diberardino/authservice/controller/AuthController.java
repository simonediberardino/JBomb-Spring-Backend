package com.diberardino.authservice.controller;

import com.diberardino.authservice.dto.LoginRequest;
import com.diberardino.authservice.dto.LoginResponse;
import com.diberardino.authservice.dto.RegisterRequest;
import com.diberardino.authservice.service.UserService;
import com.diberardino.authservice.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserService service;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, UserService service, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (service.usernameExists(req.getUsername()))
            return ResponseEntity.badRequest().body("Username already exists");

        if (service.emailExists(req.getEmail()))
            return ResponseEntity.badRequest().body("Email already exists");

        service.register(req.getUsername(), req.getEmail(), req.getPassword());
        return ResponseEntity.ok("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {

        Authentication authentication;
        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getUsernameOrEmail(),
                            req.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication.getName());

        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @GetMapping("/me")
    public ResponseEntity<?> profile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hello, " + auth.getName());
    }
}

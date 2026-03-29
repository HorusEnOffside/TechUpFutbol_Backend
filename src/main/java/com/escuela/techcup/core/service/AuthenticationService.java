package com.escuela.techcup.core.service;

import com.escuela.techcup.core.model.User;
import com.escuela.techcup.controller.dto.LoginRequest;
import com.escuela.techcup.controller.dto.LoginResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.escuela.techcup.core.exception.TechcupException;

import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserService userService,
                                 JwtService jwtService,
                                 PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userService.getUserByMail(request.getEmail())
                .orElseThrow(() -> new TechcupException("Invalid email or password", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new TechcupException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getMail(),
                user.getRoles().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet())
        );

        return new LoginResponse(token, user.getId(), user.getMail(), user.getRoles());
    }
}
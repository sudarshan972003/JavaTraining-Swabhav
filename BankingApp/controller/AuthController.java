package com.aurionpro.BankingApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.BankingApp.dto.AdminRegisterRequest;
import com.aurionpro.BankingApp.dto.ApiResponse;
import com.aurionpro.BankingApp.dto.AuthRequest;
import com.aurionpro.BankingApp.dto.AuthResponse;
import com.aurionpro.BankingApp.dto.RegisterRequest;
import com.aurionpro.BankingApp.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest req) {
        return authService.login(req);
    }

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterRequest req) {
        return authService.register(req);
    }
    
    @PostMapping("/register-admin")
    public ApiResponse registerAdmin(@RequestBody @Valid AdminRegisterRequest request) {
        return authService.registerAdmin(request);
    }
}

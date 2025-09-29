package com.aurionpro.BankingApp.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.aurionpro.BankingApp.dto.AdminRegisterRequest;
import com.aurionpro.BankingApp.dto.ApiResponse;
import com.aurionpro.BankingApp.dto.AuthRequest;
import com.aurionpro.BankingApp.dto.AuthResponse;
import com.aurionpro.BankingApp.dto.RegisterRequest;

public interface AuthService extends UserDetailsService {
    AuthResponse login(AuthRequest request);
    ApiResponse register(RegisterRequest request);
    ApiResponse registerAdmin(AdminRegisterRequest request);
}
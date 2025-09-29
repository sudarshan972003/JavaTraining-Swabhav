package com.aurionpro.BankingApp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminRegisterRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}

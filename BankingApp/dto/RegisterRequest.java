package com.aurionpro.BankingApp.dto;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String contactNo;

    @NotNull
    private LocalDate dob;

    @Valid
    private AddressDTO address;
}

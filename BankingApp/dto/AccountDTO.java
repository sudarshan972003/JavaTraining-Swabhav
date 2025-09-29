package com.aurionpro.BankingApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountDTO {
    private Long accountId;
    private String accountNumber;
    @NotBlank
    private String accountType;
    @NotNull
    private Double balance;
    private Long customerId;
}

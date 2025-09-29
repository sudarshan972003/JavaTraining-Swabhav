package com.aurionpro.BankingApp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransferRequestDTO {
    @NotNull
    private Long fromAccountId;

    @NotNull
    private String toAccountNumber;

    @NotNull
    @Min(value = 1, message = "Amount must be greater than 0")
    private Double amount;

//    @NotNull
    private Long customerId;
}

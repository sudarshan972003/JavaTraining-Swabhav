package com.aurionpro.BankingApp.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionDTO {
    private Long transId;
    @NotBlank
    private String transType;
    @NotNull
    private Double amount;
    private LocalDateTime date;
    private Long accountId;
    private Long customerId;
//    private Double balanceAfterTransaction;
}

package com.aurionpro.BankingApp.service;

import java.util.List;

import com.aurionpro.BankingApp.dto.TransactionDTO;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO dto);
    List<TransactionDTO> getTransactionsByAccountId(Long accountId);
    List<TransactionDTO> getTransactionsByCustomerId(Long customerId);
}

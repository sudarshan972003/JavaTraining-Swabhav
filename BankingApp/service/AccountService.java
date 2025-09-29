package com.aurionpro.BankingApp.service;

import java.util.List;

import com.aurionpro.BankingApp.dto.AccountDTO;
import com.aurionpro.BankingApp.dto.ApiResponse;

public interface AccountService {
    AccountDTO createAccount(AccountDTO dto);
    AccountDTO getAccountById(Long id);
    List<AccountDTO> getAccountsByCustomerId(Long customerId);
    List<AccountDTO> getAllAccounts();
    ApiResponse deleteAccount(Long id);
}

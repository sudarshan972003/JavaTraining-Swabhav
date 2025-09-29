package com.aurionpro.BankingApp.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurionpro.BankingApp.dto.AccountDTO;
import com.aurionpro.BankingApp.dto.ApiResponse;
import com.aurionpro.BankingApp.entity.Account;
import com.aurionpro.BankingApp.entity.Customer;
import com.aurionpro.BankingApp.exception.ResourceNotFoundException;
import com.aurionpro.BankingApp.repository.AccountRepository;
import com.aurionpro.BankingApp.repository.CustomerRepository;
import com.aurionpro.BankingApp.util.EmailService;

import jakarta.transaction.Transactional;

@Service
public class AccountServiceImpl implements com.aurionpro.BankingApp.service.AccountService {

    @Autowired private AccountRepository accountRepo;
    @Autowired private CustomerRepository customerRepo;
    @Autowired private EmailService emailService;

    private AccountDTO toDto(Account a) {
        var dto = new AccountDTO();
        dto.setAccountId(a.getAccountId());
        dto.setAccountNumber(a.getAccountNumber());
        dto.setAccountType(a.getAccountType());
        dto.setBalance(a.getBalance());
        dto.setCustomerId(a.getCustomer().getCustomerId());
        return dto;
    }

    @Override
    @Transactional
    public AccountDTO createAccount(AccountDTO dto) {
        Customer c = customerRepo.findById(dto.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Account acc = new Account();
        acc.setAccountNumber(generateAccountNumber());
        acc.setAccountType(dto.getAccountType());
        acc.setBalance(dto.getBalance() == null ? 0.0 : dto.getBalance());
        acc.setCustomer(c);
        accountRepo.save(acc);
        
        String subject = "New Account Created - BankingApp";
        String body = String.format(
                "Dear %s,\n\nYour new account has been successfully created.\n\nAccount ID: %d\nAccount Number: %s\nAccount Type: %s\nBalance: %.2f\n\nThank you for banking with us!",
                c.getUser().getUsername(), acc.getAccountId(), acc.getAccountNumber(), acc.getAccountType(), acc.getBalance()
        );

        emailService.sendSimpleMessage(c.getEmail(), subject, body);
        
        return toDto(acc);
    }

    private String generateAccountNumber() {
        String digits = UUID.randomUUID().toString().replaceAll("[^0-9]", "");
        if (digits.length() < 12) digits = String.format("%012d", Math.abs(digits.hashCode()));
        return digits.substring(0, 12);
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        return accountRepo.findById(id).map(this::toDto).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Override
    public List<AccountDTO> getAccountsByCustomerId(Long customerId) {
        return accountRepo.findByCustomer_CustomerId(customerId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ApiResponse deleteAccount(Long id) {
        Account acc = accountRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        accountRepo.delete(acc);
        return new ApiResponse("Account Deleted Successfully");
    }
}

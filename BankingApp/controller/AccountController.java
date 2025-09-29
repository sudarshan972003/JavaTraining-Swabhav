package com.aurionpro.BankingApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.BankingApp.dto.AccountDTO;
import com.aurionpro.BankingApp.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired private AccountService accountService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AccountDTO create(@Valid @RequestBody AccountDTO dto) {
        return accountService.createAccount(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isOwner(#id, authentication.name)")
    public AccountDTO get(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isOwnerByCustomerId(#customerId, authentication.name)")
    public List<AccountDTO> getByCustomer(@PathVariable Long customerId) {
        return accountService.getAccountsByCustomerId(customerId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountDTO> all() {
        return accountService.getAllAccounts();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }
}

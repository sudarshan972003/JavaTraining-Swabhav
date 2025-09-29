package com.aurionpro.BankingApp.security;

import org.springframework.stereotype.Component;

import com.aurionpro.BankingApp.repository.AccountRepository;

@Component("accountSecurity")
public class AccountSecurity {

    private final AccountRepository accountRepo;

    public AccountSecurity(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    public boolean isOwner(Long accountId, String username) {
        return accountRepo.findById(accountId)
                .map(a -> a.getCustomer().getUser().getUsername().equals(username))
                .orElse(false);
    }

    public boolean isOwnerByCustomerId(Long customerId, String username) {
        return accountRepo.findByCustomer_CustomerId(customerId)
                .stream()
                .allMatch(a -> a.getCustomer().getUser().getUsername().equals(username));
    }
}

package com.aurionpro.BankingApp.security;

import org.springframework.stereotype.Component;

import com.aurionpro.BankingApp.repository.CustomerRepository;

@Component("customerSecurity")
public class CustomerSecurity {

    private final CustomerRepository customerRepo;

    public CustomerSecurity(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    public boolean isOwner(Long customerId, String username) {
        return customerRepo.findById(customerId)
                .map(c -> c.getUser().getUsername().equals(username))
                .orElse(false);
    }
}

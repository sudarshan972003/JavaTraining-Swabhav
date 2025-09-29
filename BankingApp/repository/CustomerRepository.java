package com.aurionpro.BankingApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.BankingApp.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    List<Customer> findAllByUser_UserId(Long userId);
    Optional<Customer> findByUser_Username(String username);
}

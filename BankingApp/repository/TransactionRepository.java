package com.aurionpro.BankingApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.BankingApp.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount_AccountId(Long accountId);
    List<Transaction> findByCustomer_CustomerId(Long customerId);
}

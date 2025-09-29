package com.aurionpro.BankingApp.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transId;

    @Column(nullable = false)
    @Pattern(regexp = "debit|credit|transfer", message = "Transaction type must be debit, credit, or transfer")
    private String transType;

    @Column(nullable = false)
    @Min(value = 1, message = "Transaction amount must be at least 1")
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime date;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "account_id", nullable=false)
	private Account account;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "customer_id", nullable=false)
	private Customer customer;
}

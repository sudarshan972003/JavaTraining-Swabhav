package com.aurionpro.BankingApplication.model;

import java.time.LocalDateTime;

public class Transaction {
	private int id;
    private String accountNumber;
    private String type;
    private double amount;
    private LocalDateTime createdAt;
    private double balanceAfter;
    private String counterparty;
    
    public Transaction() {
    	
    }
    
    public Transaction(String accountNumber, String type, double amount, LocalDateTime createdAt, double balanceAfter, String counterparty) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
        this.balanceAfter = balanceAfter;
        this.counterparty = counterparty;
    }
    
	public Transaction(int id, String accountNumber, String type, double amount, LocalDateTime createdAt, double balanceAfter, String counterparty) {
		super();
		this.id = id;
		this.accountNumber = accountNumber;
		this.type = type;
		this.amount = amount;
		this.createdAt = createdAt;
        this.balanceAfter = balanceAfter;
        this.counterparty = counterparty;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public double getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(double balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	public String getCounterparty() {
		return counterparty;
	}

	public void setCounterparty(String counterparty) {
		this.counterparty = counterparty;
	}
}

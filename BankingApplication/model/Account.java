package com.aurionpro.BankingApplication.model;

public class Account {
	private String accountNumber;
    private String userId;
    private String name;
    private String password;
    private double balance;
    private boolean active;
    
    public Account() {
	}
    
	public Account(String accountNumber, String userId, String name, String password, double balance, boolean active) {
		super();
		this.accountNumber = accountNumber;
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.balance = balance;
		this.active = active;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}

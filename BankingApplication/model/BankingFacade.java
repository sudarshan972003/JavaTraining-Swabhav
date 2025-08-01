package com.aurionpro.BankingApplication.model;

import java.util.List;

public class BankingFacade {
	private final AccountService accountService = new AccountService();
    private final TransactionService transactionService = new TransactionService();

    public Account createAccount(String userId, String name, String password, double balance) throws Exception {
        return accountService.createAccount(userId, name, password, balance);
    }

    public List<Account> getActiveAccounts(String userId) throws Exception {
        return accountService.getActiveAccountsByUserId(userId);
    }

    public Account getAccount(String accountNumber) throws Exception {
        return accountService.getAccountByNumber(accountNumber);
    }

    public boolean validatePassword(Account account, String rawPassword) {
        return accountService.isPasswordValid(account, rawPassword);
    }

    public void deposit(Account account, int amount) throws Exception {
        transactionService.deposit(account, amount);
    }

    public void withdraw(Account account, int amount) throws Exception {
        transactionService.withdraw(account, amount);
    }

    public void transfer(Account sender, Account receiver, double amount) throws Exception {
        transactionService.transfer(sender, receiver, amount);
    }

    public List<Transaction> getTransactionHistory(String accountNumber) throws Exception {
        return transactionService.getTransactionHistory(accountNumber);
    }

    public void updateAccount(Account account) throws Exception {
        accountService.updateAccount(account);
    }
}

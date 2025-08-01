package com.aurionpro.BankingApplication.model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class AccountService {
	private final AccountDao accountDao = new AccountDao();
	private final TransactionDao transactionDao = new TransactionDao();

    public Account createAccount(String userId, String name, String password, double balance) throws Exception {
        validateName(name);
        String hashedPassword = PasswordUtil.hashPassword(password);
        String accountNumber = generateUniqueAccountNumber();
        Account account = new Account(accountNumber, userId, name, hashedPassword, balance, true);
        accountDao.save(account);
        transactionDao.save(new Transaction(account.getAccountNumber(), "DEPOSIT", balance,
                LocalDateTime.now(), account.getBalance(), "-"));
        return account;
    }

    public List<Account> getActiveAccountsByUserId(String userId) throws SQLException {
        return accountDao.findActiveAccountsByUserId(userId);
    }

    public Account getAccountByNumber(String accountNumber) throws SQLException, AccountNotFoundException {
        Account acc = accountDao.findByAccountNumber(accountNumber);
        if (acc == null) throw new AccountNotFoundException("Account not found.");
        return acc;
    }

    public void updateAccount(Account account) throws SQLException {
        accountDao.update(account);
    }

    public boolean isPasswordValid(Account account, String rawPassword) {
        return PasswordUtil.checkPassword(rawPassword, account.getPassword());
    }

    private void validateName(String name) throws ValidationException {
        if (!name.matches("^[a-zA-Z ]+$")) {
            throw new ValidationException("Name must not contain digits or special characters.");
        }
    }

    private String generateUniqueAccountNumber() throws SQLException {
        Random rand = new Random();
        String accNum;
        do {
            accNum = String.format("%012d", Math.abs(rand.nextLong()) % 1_000_000_000_000L);
        } while (accountDao.findByAccountNumber(accNum) != null);
        return accNum;
    }
}

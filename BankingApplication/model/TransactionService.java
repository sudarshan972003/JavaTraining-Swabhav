package com.aurionpro.BankingApplication.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionService {
	private final AccountDao accountDao = new AccountDao();
    private final TransactionDao transactionDao = new TransactionDao();

    public void deposit(Account account, int amount) throws Exception {
        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            account.setBalance(account.getBalance() + amount);
            accountDao.update(account);

            transactionDao.save(new Transaction(
                    account.getAccountNumber(),
                    "DEPOSIT",
                    amount,
                    LocalDateTime.now(),
                    account.getBalance(),
                    "-"
            ));
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public void withdraw(Account account, int amount) throws Exception {
        if (amount > account.getBalance())
            throw new ValidationException("Insufficient balance.");

        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            account.setBalance(account.getBalance() - amount);
            accountDao.update(account);

            transactionDao.save(new Transaction(
                    account.getAccountNumber(),
                    "WITHDRAW",
                    amount,
                    LocalDateTime.now(),
                    account.getBalance(),
                    "-"
            ));
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public void transfer(Account sender, Account receiver, double amount) throws Exception {
        if (sender.getAccountNumber().equals(receiver.getAccountNumber()))
            throw new ValidationException("Sender and receiver cannot be same.");

        if (amount > sender.getBalance())
            throw new ValidationException("Insufficient balance to transfer.");

        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + amount);
            accountDao.update(sender);
            accountDao.update(receiver);

            transactionDao.save(new Transaction(
                    sender.getAccountNumber(),
                    "TRANSFER_OUT",
                    amount,
                    LocalDateTime.now(),
                    sender.getBalance(),
                    receiver.getAccountNumber()
            ));

            transactionDao.save(new Transaction(
                    receiver.getAccountNumber(),
                    "TRANSFER_IN",
                    amount,
                    LocalDateTime.now(),
                    receiver.getBalance(),
                    sender.getAccountNumber()
            ));

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public List<Transaction> getTransactionHistory(String accountNumber) throws SQLException {
        return transactionDao.findByAccount(accountNumber);
    }
}

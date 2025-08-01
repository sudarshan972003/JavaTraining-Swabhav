package com.aurionpro.BankingApplication.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {
	public void save(Account account) throws SQLException {
        String query = "INSERT INTO accounts (account_number, user_id, name, password, balance, active) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, account.getUserId());
            stmt.setString(3, account.getName());
            stmt.setString(4, account.getPassword());
            stmt.setDouble(5, account.getBalance());
            stmt.setBoolean(6, account.isActive());
            stmt.executeUpdate();
        }
    }

    public Account findByAccountNumber(String accountNumber) throws SQLException {
        String query = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public List<Account> findActiveAccountsByUserId(String userId) throws SQLException {
        String query = "SELECT * FROM accounts WHERE user_id = ? AND active = TRUE";
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                accounts.add(map(rs));
            }
        }
        return accounts;
    }

    public void update(Account account) throws SQLException {
        String query = "UPDATE accounts SET name = ?, password = ?, balance = ?, active = ? WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, account.getName());
            stmt.setString(2, account.getPassword());
            stmt.setDouble(3, account.getBalance());
            stmt.setBoolean(4, account.isActive());
            stmt.setString(5, account.getAccountNumber());
            stmt.executeUpdate();
        }
    }

    private Account map(ResultSet rs) throws SQLException {
        Account acc = new Account();
        acc.setAccountNumber(rs.getString("account_number"));
        acc.setUserId(rs.getString("user_id"));
        acc.setName(rs.getString("name"));
        acc.setPassword(rs.getString("password"));
        acc.setBalance(rs.getDouble("balance"));
        acc.setActive(rs.getBoolean("active"));
        return acc;
    }
}

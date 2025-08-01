package com.aurionpro.BankingApplication.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
	public void save(Transaction tx) throws SQLException {
        String query = "INSERT INTO transactions (account_number, type, amount, timestamp, balance_after, counterparty) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tx.getAccountNumber());
            stmt.setString(2, tx.getType());
            stmt.setDouble(3, tx.getAmount());
            stmt.setTimestamp(4, Timestamp.valueOf(tx.getCreatedAt()));
            stmt.setDouble(5, tx.getBalanceAfter());
            stmt.setString(6, tx.getCounterparty());
            stmt.executeUpdate();
        }
    }

    public List<Transaction> findByAccount(String accountNumber) throws SQLException {
        List<Transaction> txs = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE account_number = ? ORDER BY timestamp DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Transaction tx = new Transaction();
                tx.setId(rs.getInt("id"));
                tx.setAccountNumber(rs.getString("account_number"));
                tx.setType(rs.getString("type"));
                tx.setAmount(rs.getDouble("amount"));
                tx.setCreatedAt(rs.getTimestamp("timestamp").toLocalDateTime());
                tx.setBalanceAfter(rs.getDouble("balance_after"));
                tx.setCounterparty(rs.getString("counterparty"));
                txs.add(tx);
            }
        }
        return txs;
    }
}

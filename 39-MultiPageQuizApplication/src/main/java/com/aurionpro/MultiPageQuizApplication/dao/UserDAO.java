package com.aurionpro.MultiPageQuizApplication.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.aurionpro.MultiPageQuizApplication.Util.DBUtil;
import com.aurionpro.MultiPageQuizApplication.model.User;

public class UserDAO {
	public User findByEmail(String email) throws SQLException {
		String sql = "SELECT * FROM users WHERE email=?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return map(rs);
			}
		}
		return null;
	}

	public int create(User u) throws SQLException {
		String sql = "INSERT INTO users(username,email,password_hash) VALUES (?,?,?)";
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getEmail());
			ps.setString(3, u.getPasswordHash());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					return rs.getInt(1);
			}
		}
		return -1;
	}

	public ResultSet listAllRaw(Connection c) throws SQLException {
		PreparedStatement ps = c
				.prepareStatement("SELECT id,username,email,created_at FROM users ORDER BY created_at DESC");
		return ps.executeQuery();
	}

	public void deleteById(int id) throws SQLException {
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id=?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}

	private User map(ResultSet rs) throws SQLException {
		User u = new User();
		u.setId(rs.getInt("id"));
		u.setUsername(rs.getString("username"));
		u.setEmail(rs.getString("email"));
		u.setPasswordHash(rs.getString("password_hash"));
		return u;
	}
}
package com.aurionpro.MultiPageQuizApplication.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aurionpro.MultiPageQuizApplication.Util.DBUtil;

public class AdminDAO {
	public boolean validate(String email, String hash) throws SQLException {
		String sql = "SELECT 1 FROM admins WHERE email=? AND password_hash=?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setString(2, hash);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}
}

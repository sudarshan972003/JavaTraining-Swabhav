package com.aurionpro.MultiPageQuizApplication.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aurionpro.MultiPageQuizApplication.Util.DBUtil;

public class SettingDAO {
	public Integer getNumQuestions(int categoryId) throws SQLException {
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = c
						.prepareStatement("SELECT num_questions FROM quiz_settings WHERE category_id=?")) {
			ps.setInt(1, categoryId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getInt(1);
			}
		}
		return null;
	}

	public void upsert(int categoryId, int n) throws SQLException {
		String sql = "INSERT INTO quiz_settings(category_id,num_questions) VALUES (?,?) ON DUPLICATE KEY UPDATE num_questions=VALUES(num_questions)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, categoryId);
			ps.setInt(2, n);
			ps.executeUpdate();
		}
	}
}

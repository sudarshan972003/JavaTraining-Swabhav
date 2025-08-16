package com.aurionpro.MultiPageQuizApplication.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aurionpro.MultiPageQuizApplication.Util.DBUtil;
import com.aurionpro.MultiPageQuizApplication.model.Result;

public class ResultDAO {
	public void save(Result r) throws SQLException {
	    String sql = "INSERT INTO results(user_id, category_id, score, total) VALUES (?,?,?,?)";
	    try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
	        ps.setInt(1, r.getUserId());
	        ps.setInt(2, r.getCategoryId());
	        ps.setInt(3, r.getScore());
	        ps.setInt(4, r.getTotal());
	        ps.executeUpdate();
	    }
	}
	
	public Result[] findByUser(int userId) throws SQLException {
		String sql = "SELECT r.id, r.user_id, r.category_id, c.name AS category_name, r.score, r.total, r.taken_at " +
	             "FROM results r JOIN categories c ON r.category_id = c.id " +
	             "WHERE r.user_id = ? ORDER BY r.taken_at DESC";
	    try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
	        ps.setInt(1, userId);
	        try (ResultSet rs = ps.executeQuery()) {
	            rs.last();
	            int count = rs.getRow();
	            rs.beforeFirst();
	            Result[] results = new Result[count];
	            int i = 0;
	            while (rs.next()) {
	                Result r = new Result();
	                r.setId(rs.getInt("id"));
	                r.setUserId(rs.getInt("user_id"));
	                r.setCategoryId(rs.getInt("category_id"));
	                r.setCategoryName(rs.getString("category_name"));
	                r.setScore(rs.getInt("score"));
	                r.setTotal(rs.getInt("total"));
	                r.setTakenAt(rs.getTimestamp("taken_at"));
	                results[i++] = r;
	            }
	            return results;
	        }
	    }
	}
}

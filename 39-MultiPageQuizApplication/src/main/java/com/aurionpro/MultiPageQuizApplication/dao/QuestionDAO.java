package com.aurionpro.MultiPageQuizApplication.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.MultiPageQuizApplication.Util.DBUtil;
import com.aurionpro.MultiPageQuizApplication.model.Question;

public class QuestionDAO {
	private Question map(ResultSet rs) throws SQLException {
		Question q = new Question();
		q.setId(rs.getInt("id"));
		q.setCategoryId(rs.getInt("category_id"));
		q.setText(rs.getString("question_text"));
		q.setOptA(rs.getString("opt_a"));
		q.setOptB(rs.getString("opt_b"));
		q.setOptC(rs.getString("opt_c"));
		q.setOptD(rs.getString("opt_d"));
		q.setCorrect(rs.getString("correct_opt"));
		return q;
	}

	public void create(Question q) throws SQLException {
		String sql = "INSERT INTO questions(category_id,question_text,opt_a,opt_b,opt_c,opt_d,correct_opt) VALUES (?,?,?,?,?,?,?)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, q.getCategoryId());
			ps.setString(2, q.getText());
			ps.setString(3, q.getOptA());
			ps.setString(4, q.getOptB());
			ps.setString(5, q.getOptC());
			ps.setString(6, q.getOptD());
			ps.setString(7, q.getCorrect());
			ps.executeUpdate();
		}
	}

	public List<Question> byCategory(int catId) throws SQLException {
		List<Question> list = new ArrayList<>();
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = c.prepareStatement("SELECT * FROM questions WHERE category_id=? ORDER BY id")) {
			ps.setInt(1, catId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					list.add(map(rs));
			}
		}
		return list;
	}

	public Question find(int id) throws SQLException {
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = c.prepareStatement("SELECT * FROM questions WHERE id=?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return map(rs);
			}
		}
		return null;
	}

	public void update(Question q) throws SQLException {
		String sql = "UPDATE questions SET category_id=?,question_text=?,opt_a=?,opt_b=?,opt_c=?,opt_d=?,correct_opt=? WHERE id=?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, q.getCategoryId());
			ps.setString(2, q.getText());
			ps.setString(3, q.getOptA());
			ps.setString(4, q.getOptB());
			ps.setString(5, q.getOptC());
			ps.setString(6, q.getOptD());
			ps.setString(7, q.getCorrect());
			ps.setInt(8, q.getId());
			ps.executeUpdate();
		}
	}

	public void delete(int id) throws SQLException {
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM questions WHERE id=?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}

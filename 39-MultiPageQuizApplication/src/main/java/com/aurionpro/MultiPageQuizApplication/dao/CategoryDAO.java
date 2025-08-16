package com.aurionpro.MultiPageQuizApplication.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.MultiPageQuizApplication.Util.DBUtil;
import com.aurionpro.MultiPageQuizApplication.model.Category;

public class CategoryDAO {
	public List<Category> all() throws SQLException {
		List<Category> list = new ArrayList<>();
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = c.prepareStatement("SELECT * FROM categories ORDER BY name");
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Category cat = new Category();
				cat.setId(rs.getInt("id"));
				cat.setName(rs.getString("name"));
				list.add(cat);
			}
		}
		return list;
	}

	public Category findById(int id) throws SQLException {
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = c.prepareStatement("SELECT * FROM categories WHERE id=?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Category cat = new Category();
					cat.setId(id);
					cat.setName(rs.getString("name"));
					return cat;
				}
			}
		}
		return null;
	}

	public int upsertByName(String name) throws SQLException {
		try (Connection c = DBUtil.getConnection()) {
			try (PreparedStatement g = c.prepareStatement("SELECT id FROM categories WHERE name=?")) {
				g.setString(1, name);
				try (ResultSet rs = g.executeQuery()) {
					if (rs.next())
						return rs.getInt(1);
				}
			}
			try (PreparedStatement ins = c.prepareStatement("INSERT INTO categories(name) VALUES (?)",
					Statement.RETURN_GENERATED_KEYS)) {
				ins.setString(1, name);
				ins.executeUpdate();
				try (ResultSet rs = ins.getGeneratedKeys()) {
					if (rs.next())
						return rs.getInt(1);
				}
			}
		}
		return -1;
	}
	
	public boolean update(Category category) throws SQLException {
	    try (Connection c = DBUtil.getConnection();
	         PreparedStatement ps = c.prepareStatement("UPDATE categories SET name=? WHERE id=?")) {
	        ps.setString(1, category.getName());
	        ps.setInt(2, category.getId());
	        return ps.executeUpdate() > 0;
	    }
	}

	public boolean delete(int id) throws SQLException {
	    try (Connection c = DBUtil.getConnection();
	         PreparedStatement ps = c.prepareStatement("DELETE FROM categories WHERE id=?")) {
	        ps.setInt(1, id);
	        return ps.executeUpdate() > 0;
	    }
	}
}

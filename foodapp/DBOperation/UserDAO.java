package com.foodapp.DBOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.foodapp.Service.MyConnection;
import com.foodapp.Service.PasswordUtil;
import com.foodapp.entity.User;

public class UserDAO {
	public void save(User user) throws ClassNotFoundException {
        String sql = "INSERT INTO user (first_name, last_name, address, email, mobile, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = MyConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getAddress());
            stmt.setString(4, user.getEmailId());
            stmt.setString(5, user.getMobileNo());
            stmt.setString(6, PasswordUtil.hashPassword(user.getPassword()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findByEmailOrMobile(String input, String password) throws ClassNotFoundException {
        String sql = "SELECT * FROM user WHERE (email = ? OR mobileno = ?) AND password = ?";
        try (Connection conn = MyConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, input);
            stmt.setString(2, input);
            stmt.setString(3, PasswordUtil.hashPassword(password));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("userid"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setEmailId(rs.getString("email"));
                user.setMobileNo(rs.getString("mobileno"));
                user.setAddress(rs.getString("address"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<User> findAll() throws ClassNotFoundException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Connection conn = MyConnection.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("userid"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setEmailId(rs.getString("email"));
                user.setMobileNo(rs.getString("mobileno"));
                user.setAddress(rs.getString("address"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public void deleteUserFromDB(int userId) throws ClassNotFoundException {
    	String sql = "DELETE FROM user WHERE userid = ?";

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("No user found with ID: " + userId);
            }

        } catch (SQLException e) {
            System.out.println("Error while deleting user: " + e.getMessage());
        }
    }
}

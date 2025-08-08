package com.FoodOrderingConsoleApp.DBOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.FoodOrderingConsoleApp.Service.MyConnection;
import com.FoodOrderingConsoleApp.Service.PasswordUtil;
import com.FoodOrderingConsoleApp.entity.User;

public class UserDAO {
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
    
    public void addUser(User user) {
	    String query = "INSERT INTO user (firstName, lastName, email, address, password, mobileno) VALUES (?, ?, ?, ?, ?, ?)";

	    try (Connection conn = MyConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setString(1, user.getFirstName());
	        ps.setString(2, user.getLastName());
	        ps.setString(3, user.getEmailId());
	        ps.setString(4, user.getAddress());
	        ps.setString(5, user.getPassword()); 
	        ps.setString(6, user.getMobileNo());

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            System.out.println("User registered successfully in database.");
	        } else {
	            System.out.println("Registration failed.");
	        }

	    } catch (Exception e) {
	        System.out.println("Error during registration: " + e.getMessage());
	    }
	}

	public static boolean emailExists(String email) {
	    String query = "SELECT email FROM user WHERE email = ?";

	    try (Connection conn = MyConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setString(1, email);
	        ResultSet rs = ps.executeQuery();
	        if(rs.next()) {
	        	System.out.println("Already Email exits!!");
	        	return false;
	        }
	        return true;

	    } catch (Exception e) {
	        System.out.println("Error checking email existence: " + e.getMessage());
	        return false;  
	    }
	}
	
	public static boolean mobileExists(String mobile) {
	    String query = "SELECT email FROM user WHERE mobileno = ?";

	    try (Connection conn = MyConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setString(1, mobile);
	        ResultSet rs = ps.executeQuery();
	        if(rs.next()) {
	        	System.out.println("Mobile Number Email exits!!");
	        	return false;
	        }
	        return true;

	    } catch (Exception e) {
	        System.out.println("Error checking Mobile Number existence: " + e.getMessage());
	        return false;  
	    }
	}

	public static boolean validateUser(String email, String password) {
	    String query = "SELECT * FROM user WHERE email = ? AND password = ?";

	    try (Connection conn = MyConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setString(1, email);
	        ps.setString(2, password); 

	        ResultSet rs = ps.executeQuery();

	        return rs.next();

	    } catch (Exception e) {
	        System.out.println("Error while validating user: " + e.getMessage());
	        return false;
	    }
	}

	public static int userId(String email, String password) {
	    String query = "SELECT userid FROM user WHERE email = ? AND password = ?";

	    try (Connection conn = MyConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setString(1, email);
	        ps.setString(2, password); 

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("userid");
	        } else {
	            return -1;
	        }

	    } catch (Exception e) {
	        System.out.println("Error fetching userId: " + e.getMessage());
	        return -1; 
	    }
	}
}

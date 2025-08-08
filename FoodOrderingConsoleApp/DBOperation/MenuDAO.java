package com.FoodOrderingConsoleApp.DBOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.FoodOrderingConsoleApp.Service.MyConnection;
import com.FoodOrderingConsoleApp.entity.MenuItem;

public class MenuDAO {
	
	public List<MenuItem> findAll() throws ClassNotFoundException {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu";

        try (Connection conn = MyConnection.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                items.add(new MenuItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void addItem(MenuItem item) throws ClassNotFoundException {
        String sql = "INSERT INTO menu (name, price) VALUES (?, ?)";
        try (Connection conn = MyConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteItem(int id) throws ClassNotFoundException {
        String sql = "DELETE FROM menu WHERE id = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item deleted successfully.");
            } else {
                System.out.println("No item found with ID: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}
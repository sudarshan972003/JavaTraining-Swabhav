package com.FoodOrderingConsoleApp.DBOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.FoodOrderingConsoleApp.Service.MyConnection;
import com.FoodOrderingConsoleApp.entity.DeliveryPartner;

public class DeliveryPartnerDAO {
	public List<DeliveryPartner> findAll() throws ClassNotFoundException {
        List<DeliveryPartner> partners = new ArrayList<>();
        String sql = "SELECT * FROM delivery_partners";

        try (Connection conn = MyConnection.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                partners.add(new DeliveryPartner(
                    rs.getInt("id"),
                    rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partners;
    }

    public void addPartner(String name) throws ClassNotFoundException {
        String sql = "INSERT INTO delivery_partners (name) VALUES (?)";
        try (Connection conn = MyConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deletePartner(int id) throws ClassNotFoundException {
        String sql = "DELETE FROM delivery_partners WHERE id = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Delivery Partner deleted successfully.");
            } else {
                System.out.println("No Delivery Partner found with ID: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

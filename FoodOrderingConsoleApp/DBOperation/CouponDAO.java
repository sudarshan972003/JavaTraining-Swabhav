package com.FoodOrderingConsoleApp.DBOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.FoodOrderingConsoleApp.Service.MyConnection;
import com.FoodOrderingConsoleApp.entity.Coupon;

public class CouponDAO {
	public Coupon findByCode(String code) throws ClassNotFoundException {
        String sql = "SELECT * FROM coupons WHERE code = ?";
        try (Connection conn = MyConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Coupon(
                    rs.getString("code"),
                    rs.getDouble("discount")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addCoupon(Coupon coupon) throws ClassNotFoundException {
        String sql = "INSERT INTO coupons (code, discount) VALUES (?, ?)";
        try (Connection conn = MyConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, coupon.getCode());
            stmt.setDouble(2, coupon.getDiscount());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Coupon> findAll() throws ClassNotFoundException {
        List<Coupon> coupons = new ArrayList<>();
        String sql = "SELECT * FROM coupons";
        try (Connection conn = MyConnection.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                coupons.add(new Coupon(
                    rs.getString("code"),
                    rs.getDouble("discount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coupons;
    }

    public void deleteCoupon(String couponCode) throws ClassNotFoundException {
        String sql = "DELETE FROM coupons WHERE code = ?";
        
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, couponCode);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Coupon '" + couponCode + "' deleted successfully.");
            } else {
                System.out.println("No coupon found with code: " + couponCode);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

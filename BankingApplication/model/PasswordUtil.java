package com.aurionpro.BankingApplication.model;
import java.security.MessageDigest;

public class PasswordUtil {
	public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password");
        }
    }
	
	public static boolean checkPassword(String rawPassword, String hashedPassword) {
        String hashedRaw = hashPassword(rawPassword);
        return hashedRaw.equals(hashedPassword);
    }
}


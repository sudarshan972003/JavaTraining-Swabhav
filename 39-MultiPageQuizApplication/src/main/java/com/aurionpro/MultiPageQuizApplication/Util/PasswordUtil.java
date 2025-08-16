package com.aurionpro.MultiPageQuizApplication.Util;

import java.security.MessageDigest;

public class PasswordUtil {
	public static String sha256(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] d = md.digest(s.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (byte b : d)
				sb.append(String.format("%02x", b));
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

package com.aurionpro.MultiPageQuizApplication.Util;

public class ValidationUtil {
	
	public static void require(String v, String f) {
		if (v == null || v.trim().isEmpty())
			throw new IllegalArgumentException(f + " required");
	}

	public static void password(String v) {
		require(v, "Password");
		if (v.length() < 6)
			throw new IllegalArgumentException("Password too short");
	}
}

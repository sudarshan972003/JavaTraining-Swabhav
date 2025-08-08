package com.FoodOrderingConsoleApp.Validation;

import java.util.Scanner;

import com.FoodOrderingConsoleApp.DBOperation.UserDAO;

public class Validation {
	Scanner scanner;

	public Validation(Scanner scanner) {
		this.scanner = scanner;
	}

	public String correctName(String fieldName) {
		while (true) {
			System.out.print("Enter " + fieldName + " (no spaces): ");
			String input = scanner.nextLine();
			if (input != null && input.matches("^[A-Za-z]+$")) {
				return input;
			} else {
				System.out.println("Invalid " + fieldName + ". Only alphabets allowed.");
			}
		}
	}

	public String inputNonEmpty(String fieldName) {
		while (true) {
			System.out.print("Enter " + fieldName + ": ");
			String input = scanner.nextLine();
			if (!input.trim().isEmpty()) {
				return input;
			} else {
				System.out.println(fieldName + " cannot be empty.");
			}
		}
	}

	public String inputEmail() {
		while (true) {
			System.out.print("Enter Email: ");
			String email = scanner.nextLine();
			if (email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
				if (UserDAO.emailExists(email)) {
					return email;
				}
			} else {
				System.out.println("Invalid email format.");
			}
		}
	}

	public String inputEmailForLogin() {
		while (true) {
			System.out.print("Enter Email: ");
			String email = scanner.nextLine();
			if (email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
				return email;
			} else {
				System.out.println("Invalid email format.");
			}
		}
	}

	public String inputMobile() {
		while (true) {
			System.out.print("Enter Mobile No (10 digits): ");
			String mobile = scanner.nextLine();
			if (mobile.matches("^[6-9]\\d{9}$")) {
				if (UserDAO.mobileExists(mobile)) {
					return mobile;}
			} else {
				System.out.println("Invalid mobile number. Must start with 6-9 and be 10 digits.");
			}
		}
	}

	public String inputPassword() {
		while (true) {
			System.out.print("Enter Password (min 6 chars): ");
			String pwd = scanner.nextLine();
			if (pwd.length() >= 6) {
				return pwd;
			} else {
				System.out.println("Password must be at least 6 characters.");
			}
		}
	}

}

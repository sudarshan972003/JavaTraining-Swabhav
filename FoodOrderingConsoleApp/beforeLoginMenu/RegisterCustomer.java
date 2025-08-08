package com.FoodOrderingConsoleApp.beforeLoginMenu;

import java.util.Scanner;

import com.FoodOrderingConsoleApp.Service.PasswordUtil;
import com.FoodOrderingConsoleApp.Validation.Validation;
import com.FoodOrderingConsoleApp.entity.User;

public class RegisterCustomer {
	Scanner scanner;

	public RegisterCustomer(Scanner scanner) {
		this.scanner = scanner;
	}

	public User register() {
		System.out.println("\n------ Registration ------");
		scanner.nextLine();
		Validation validate = new Validation(scanner);
		String firstName = validate.correctName("First name");
		String lastName = validate.correctName("Last name");

		String address = validate.inputNonEmpty("Address");

		String email = validate.inputEmail();
		String mobile = validate.inputMobile();
		String password = validate.inputPassword();

		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setAddress(address);
		user.setEmailId(email);
		user.setMobileNo(mobile);
		user.setPassword(PasswordUtil.hashPassword(password));

		System.out.println("Registration successful!");
		return user;
	}

	
}

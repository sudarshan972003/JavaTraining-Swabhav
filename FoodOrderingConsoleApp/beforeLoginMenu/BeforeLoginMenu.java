package com.FoodOrderingConsoleApp.beforeLoginMenu;

import java.util.Scanner;

import com.FoodOrderingConsoleApp.DBOperation.UserDAO;
import com.FoodOrderingConsoleApp.entity.User;

public class BeforeLoginMenu {
	Scanner scanner = new Scanner(System.in);

	public void menuBeforeLogin() throws ClassNotFoundException {
		System.out.println("-----------------Welcome to Food Ordering App---------------");
		while (true) {
			System.out.println("\n#--Options--#");
			System.out.println("1. Login as customer");
			System.out.println("2. Login as Admin");
			System.out.println("3. Register as a Customer");
			System.out.println("4. Exit the app");
			int choice = 0;
			while (true) {
				System.out.print("Enter Your choice : ");
				if (scanner.hasNextInt()) {
					choice = scanner.nextInt();
					break;
				} else {
					System.out.println("Enter Integer only!!");
					scanner.next();
				}
			}

			switch (choice) {
			case 1:
				LoginCustomer login = new LoginCustomer(scanner);
				login.login();
				break;

			case 2:
				LoginAdmin loginAdmin = new LoginAdmin(scanner);
				loginAdmin.loginAdmin();
				break;

			case 3:
				RegisterCustomer registerCustomer = new RegisterCustomer(scanner);
				User user = registerCustomer.register();
				UserDAO addUser = new  UserDAO();
				addUser.addUser(user);
				break;

			case 4:
				System.out.println("Thank you for using our app!!");
				System.exit(0);
				break;
				
			default : System.out.println("You have entered wrong option!");
			}

		}
	}
}

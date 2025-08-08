package com.FoodOrderingConsoleApp.AfterLogin.admin;

import java.util.List;

import com.FoodOrderingConsoleApp.DBOperation.CouponDAO;
import com.FoodOrderingConsoleApp.DBOperation.DeliveryPartnerDAO;
import com.FoodOrderingConsoleApp.DBOperation.MenuDAO;
import com.FoodOrderingConsoleApp.DBOperation.UserDAO;
import com.FoodOrderingConsoleApp.Service.InputUtil;
import com.FoodOrderingConsoleApp.Service.UserPrinter;
import com.FoodOrderingConsoleApp.entity.Coupon;
import com.FoodOrderingConsoleApp.entity.DeliveryPartner;
import com.FoodOrderingConsoleApp.entity.MenuItem;
import com.FoodOrderingConsoleApp.entity.User;

public class AdminService {
	private final MenuDAO menuDao = new MenuDAO();
	private final CouponDAO couponDao = new CouponDAO();
	private final DeliveryPartnerDAO deliveryDao = new DeliveryPartnerDAO();
	private final UserDAO userDao = new UserDAO();

	private final String ADMIN_USERNAME = "admin";
	private final String ADMIN_PASSWORD = "admin123";

	public void adminLogin() throws ClassNotFoundException {
		System.out.println("\n-------- Admin Login----------");
		String username = InputUtil.getString("Enter admin username: ");
		String password = InputUtil.getString("Enter admin password: ");
		if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
			showAdminMenu();
		} else {
			System.out.println("Invalid admin credentials.");
		}
	}

	private void showAdminMenu() throws ClassNotFoundException {
		while (true) {
			System.out.println("\n--- Admin Menu ---");
			System.out.println("1. Manage Menu");
			System.out.println("2. Manage Coupons");
			System.out.println("3. Manage Delivery Partners");
			System.out.println("4. View Registered Users");
			System.out.println("5. Delete User");
			System.out.println("6. Logout");

			int choice = InputUtil.getInt("Enter choice: ");
			switch (choice) {
			case 1 -> manageMenu();
			case 2 -> manageCoupons();
			case 3 -> manageDeliveryPartners();
			case 4 -> viewAllUsers();
			case 5 -> deleteUser();
			case 6 -> {
				System.out.println("Logging out admin...");
				return;
			}
			default -> System.out.println("Invalid choice!");
			}
		}
	}

	private void deleteUser() throws ClassNotFoundException {
		int userId;

		while (true) {
			int input = InputUtil.getInt("Enter User ID to delete: ");

			try {
				userId = input;
				if (userId > 0) {
					userDao.deleteUserFromDB(userId);
					break;
				} else {
					System.out.println("User ID must be greater than 0.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a numeric User ID.");
			}
		}
	}

	private void manageMenu() throws ClassNotFoundException {
		while (true) {
			System.out.println("\n--- Manage Menu ---");
			System.out.println("1. Add Item");
			System.out.println("2. Delete Item");
			System.out.println("3. View All Items");
			System.out.println("4. Back");

			int choice = InputUtil.getInt("Enter choice: ");
			switch (choice) {
			case 1 -> {
				String name = "";
				while(true) {
					name = InputUtil.getString("Enter food name: ");
					if(name != null && name.matches("^[A-Za-z]+( [A-Za-z]+)*$")) {
						break;
					}else {
						System.out.println("Enter correct name!");
					}
				}
				double price = 0.0;
				while (true) {
					price = InputUtil.getDouble("Enter price: ");
					if(price>=1) {
						break;
					}else {
						System.out.println("Price sholud be greater than 1!");
					}
				}
				menuDao.addItem(new MenuItem(0, name, price));
				System.out.println("Item added.");
			}
			case 2 -> {
				int id = InputUtil.getInt("Enter item ID to delete: ");
				menuDao.deleteItem(id);
			}
			case 3 -> {
				List<MenuItem> items = menuDao.findAll();
				if (items.isEmpty()) {
					System.out.println("No items found.");
				} else {
					System.out.println("--- Food Menu ---");
					for (MenuItem item : items) {
						System.out.println(item);
					}
				}
			}
			case 4 -> {
				return;
			}
			default -> System.out.println("Invalid choice!");
			}
		}
	}

	private void manageCoupons() throws ClassNotFoundException {
		while (true) {
			System.out.println("\n--- Manage Coupons ---");
			System.out.println("1. Add Coupon");
			System.out.println("2. View All Coupons");
			System.out.println("3. Delete Coupon");
			System.out.println("4. Back");

			int choice = InputUtil.getInt("Enter choice: ");
			switch (choice) {
			case 1 -> {
				String code = "";
				while(true) {
					 code = InputUtil.getString("Enter coupon code: ");
					 if(code != null && !code.isBlank()) {
						 break;
					 }
				}
				double price = 0.0;
				while (true) {
					price = InputUtil.getDouble("Enter discount amount: ");
					if(price>=1) {
						break;
					}else {
						System.out.println("Price sholud be greater than 0!");
					}
				}
				couponDao.addCoupon(new Coupon(code, price));
				System.out.println("Coupon added.");
			}
			case 2 -> {
				List<Coupon> coupons = couponDao.findAll();
				if (coupons.isEmpty()) {
					System.out.println("No coupons found.");
				} else {
					System.out.println("--- Available Coupons ---");
					for (Coupon coupon : coupons) {
						System.out.println(coupon);
					}
				}
			}
			case 3-> {
		        String couponCode = InputUtil.getString("Enter Coupon Code to delete: ").trim();
		        couponDao.deleteCoupon(couponCode);
			}
			case 4 -> {
				return;
			}
			default -> System.out.println("Invalid choice!");
			}
		}
	}

	private void manageDeliveryPartners() throws ClassNotFoundException {
		while (true) {
			System.out.println("\n--- Manage Delivery Partners ---");
			System.out.println("1. Add Partner");
			System.out.println("2. View All Partners");
			System.out.println("3. Delete Partner");
			System.out.println("4. Back");

			int choice = InputUtil.getInt("Enter choice: ");
			switch (choice) {
			case 1 -> {
				String name = "";
				while(true) {
					 name = InputUtil.getString("Enter partner name: ");
					 if(name != null && !name.isBlank()) {
						 break;
					 }
				}
				deliveryDao.addPartner(name);
				System.out.println("Partner added.");
			}
			case 2 -> {
				List<DeliveryPartner> partners = deliveryDao.findAll();
				if (partners.isEmpty()) {
					System.out.println("No delivery partners found.");
				} else {
					System.out.println("--- Delivery Partners ---");
					for (DeliveryPartner partner : partners) {
						System.out.println(partner);
					}
				}
			}
			case 3 -> {
				int id = InputUtil.getInt("Enter Delivery Partner ID to delete: ");
				deliveryDao.deletePartner(id);
			}
			case 4 -> {
				return;
			}
			default -> System.out.println("Invalid choice!");
			}
		}
	}

	private void viewAllUsers() throws ClassNotFoundException {
		List<User> users = userDao.findAll();
		if (users.isEmpty()) {
			System.out.println("No users registered.");
		} else {
			System.out.println("--- Registered Users ---");
			UserPrinter.printUsers(users);
		}
	}
}

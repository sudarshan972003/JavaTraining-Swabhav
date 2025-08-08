package com.foodapp.beforeLoginMenu;

import java.util.Scanner;
import com.foodapp.DBOperation.UserDAO;
import com.foodapp.Service.InputUtil;
import com.foodapp.Service.OrderService;
import com.foodapp.entity.User;

public class LoginCustomer {
	
	private final UserDAO userDao = new UserDAO();
	 private final OrderService orderService = new OrderService();
	
	Scanner scanner;
	public LoginCustomer(Scanner scanner) {
		this.scanner = scanner;
	}
	
	public void login() throws ClassNotFoundException {
        System.out.println("\n--- User Login ---");
        String input = InputUtil.getString("Enter email or mobile: ");
        String password = InputUtil.getString("Enter password: ");

        User user = userDao.findByEmailOrMobile(input, password);
        if (user != null) {
            System.out.println("Login successful. Welcome " + user.getFirstName() + "!");
            orderService.orderMenu(user);
        } else {
            System.out.println("Invalid credentials.");
        }
    }
	

}

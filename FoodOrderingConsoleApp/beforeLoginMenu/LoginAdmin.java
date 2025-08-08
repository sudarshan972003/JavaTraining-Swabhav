package com.FoodOrderingConsoleApp.beforeLoginMenu;

import java.util.Scanner;

import com.FoodOrderingConsoleApp.AfterLogin.admin.AdminService;

public class LoginAdmin {
	
	Scanner scanner;

	public LoginAdmin(Scanner scanner) {
		this.scanner = scanner;
	}

	public void loginAdmin() throws ClassNotFoundException {
		AdminService adminService = new AdminService();
		adminService.adminLogin();
		
	}

}

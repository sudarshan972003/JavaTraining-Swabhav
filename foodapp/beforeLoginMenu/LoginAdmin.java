package com.foodapp.beforeLoginMenu;

import java.util.Scanner;

import com.foodapp.AfterLogin.admin.AdminService;

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

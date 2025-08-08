package com.FoodOrderingConsoleApp.Service;

import java.util.List;

import com.FoodOrderingConsoleApp.entity.User;

public class UserPrinter {

    public static void printUsers(List<User> users) {
        System.out.printf("%-5s %-15s %-15s %-20s %-25s %-15s%n",
                "ID", "First Name", "Last Name", "Address", "Email", "Mobile No");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (User u : users) {
            System.out.printf("%-5d %-15s %-15s %-20s %-25s %-15s%n",
                    u.getId(),
                    u.getFirstName(),
                    u.getLastName(),
                    u.getAddress(),
                    u.getEmailId(),
                    u.getMobileNo());
        }
    }
}


package com.FoodOrderingConsoleApp.Service;

import java.util.Scanner;

public class InputUtil {
	private static final Scanner sc = new Scanner(System.in);

    public static String getString(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

    public static int getInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    public static double getDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid amount.");
            }
        }
    }
}

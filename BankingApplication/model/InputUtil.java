package com.aurionpro.BankingApplication.model;

import java.io.Console;
import java.util.Scanner;

public class InputUtil {
	private static final Scanner sc = new Scanner(System.in);

	public static String readNonEmpty(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = sc.nextLine().trim();
        } while (input.isEmpty());
        return input;
    }

	public static String readValidUserId(String prompt) {
	    while (true) {
	        String userId = readNonEmpty(prompt);
	        if (userId.matches("^[a-zA-Z][a-zA-Z0-9]{3,}$")) {
	            return userId;
	        }
	        System.out.println("Invalid User ID. It must:");
	        System.out.println("- Start with a letter");
	        System.out.println("- Be at least 4 characters long");
	        System.out.println("- Contain only letters and digits");
	        System.out.println("Try again.");
	    }
	}
	
    public static int readIntInRange(String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(sc.nextLine().trim());
                if (value >= min && value <= max)
                    return value;
                System.out.println("Enter a number between " + min + " and " + max);
            } catch (Exception e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public static double readPositiveDouble(String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Double.parseDouble(sc.nextLine().trim());
                if (value > 0)
                    return value;
                System.out.println("Amount must be positive.");
            } catch (Exception e) {
                System.out.println("Invalid amount.");
            }
        }
    }

    public static int readPositiveMultipleOf10(String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(sc.nextLine().trim());
                if (value > 0 && value % 10 == 0)
                    return value;
                System.out.println("Amount must be a positive multiple of 10.");
            } catch (Exception e) {
                System.out.println("Invalid number.");
            }
        }
    }
    
    public static boolean getConfirmation(String message) {
        while (true) {
            System.out.print(message + " (Y/N): ");
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("Y") || input.equals("YES")) return true;
            if (input.equals("N") || input.equals("NO")) return false;
            System.out.println("Please enter Y or N.");
        }
    }
	
    public static String readMaskedPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] pwdArray = console.readPassword(prompt);
            return new String(pwdArray);
        } else {
            System.out.print(prompt);
            return sc.nextLine();
        }
    }
}

package com.aurionpro.GuitarApp.model;

import java.util.Arrays;
import java.util.Scanner;

public class InputHelper {
	private static final Scanner scanner = new Scanner(System.in);

    public static String getStringInput(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine().trim();
    }

    public static int getIntInput(String label) {
        while (true) {
            System.out.print(label + ": ");
            try {
                int val = Integer.parseInt(scanner.nextLine());
                if (val <= 0) throw new NumberFormatException();
                return val;
            } catch (NumberFormatException e) {
                System.out.println(label + " must be a positive number.");
            }
        }
    }

    public static double getDoubleInput(String label) {
        while (true) {
            System.out.print(label + ": ");
            try {
                double val = Double.parseDouble(scanner.nextLine());
                if (val <= 0) throw new NumberFormatException();
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Price must be a positive number. Try again.");
            }
        }
    }

    public static <T extends Enum<T>> T getEnumInput(String label, Class<T> type) {
        while (true) {
            System.out.print(label + " (" + String.join("/", enumNames(type)) + "): ");
            try {
                return Enum.valueOf(type, scanner.nextLine().trim().toUpperCase().replace(' ', '_'));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid " + label + ". Try again.");
            }
        }
    }

    private static <T extends Enum<T>> String[] enumNames(Class<T> enumType) {
        return Arrays.stream(enumType.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

    public static String getLine() {
        return scanner.nextLine();
    }
}

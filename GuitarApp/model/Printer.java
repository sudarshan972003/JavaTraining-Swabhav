package com.aurionpro.GuitarApp.model;

import java.util.List;

public class Printer {
	public static void printMenu() {
        System.out.println("\n====== Guitar Inventory Menu ======");
        System.out.println("1. Search Guitars");
        System.out.println("2. Add Guitar");
        System.out.println("3. Delete Guitar");
        System.out.println("4. View All Guitars");
        System.out.println("5. Exit");
        System.out.print("Choose an option (1-5): ");
    }

    public static void printSearchOptions() {
        System.out.println("\n--- Search Options ---");
        System.out.println("1. Search by Builder");
        System.out.println("2. Search by Model");
        System.out.println("3. Search by Type");
        System.out.println("4. Search by Number of Strings");
        System.out.println("5. Search by Back Wood");
        System.out.println("6. Search by Top Wood");
        System.out.println("7. Exit Search");
        System.out.print("Choose an option (1-7): ");
    }

    public static void printGuitar(Guitar g) {
        GuitarSpec s = g.getSpec();
        System.out.printf("Serial#: %s | $%.2f | Builder: %s, Model: %s, %d-string, Type: %s, Back: %s, Top: %s%n",
                g.getSerialNumber(), g.getPrice(), s.getBuilder(), s.getModel(),
                s.getNumStrings(), s.getType(), s.getBackWood(), s.getTopWood());
    }

    public static void printGuitarList(List<Guitar> guitars) {
        if (guitars.isEmpty()) {
            System.out.println("No guitars in inventory.");
        } else {
            guitars.forEach(Printer::printGuitar);
        }
    }
}

package com.aurionpro.GuitarApp.test;

import com.aurionpro.GuitarApp.model.GuitarService;
import com.aurionpro.GuitarApp.model.InputHelper;
import com.aurionpro.GuitarApp.model.Inventory;
import com.aurionpro.GuitarApp.model.InventoryFileManager;
import com.aurionpro.GuitarApp.model.Printer;

public class FindGuitarTester {
	
	public static void main(String[] args) {
		System.out.println("===================================================");
		System.out.println("       -- Welcome to GuitarShop Manager -- ");
		System.out.println("(Inventory Management System for Guitar Retailers)");
		System.out.println("===================================================");
		
        Inventory inventory = new Inventory();
        GuitarService service = new GuitarService();
        InventoryFileManager.initializeInventoryFromFile(inventory);

        while (true) {
            Printer.printMenu();
            String input = InputHelper.getLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
                continue;
            }

            switch (choice) {
                case 1 -> service.advancedSearchGuitars(inventory);
                case 2 -> service.addGuitar(inventory);
                case 3 -> service.deleteGuitar(inventory);
                case 4 -> service.viewAllGuitars(inventory);
                case 5 -> {
                    System.out.println("Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please enter 1-5.");
            }
        }
	}
}

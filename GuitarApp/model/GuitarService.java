package com.aurionpro.GuitarApp.model;

import java.util.List;

public class GuitarService {
	public void viewAllGuitars(Inventory inventory) {
        System.out.println("\n--- All Guitars ---");
        Printer.printGuitarList(inventory.getAllGuitars());
    }

    public void deleteGuitar(Inventory inventory) {
        System.out.println("\n--- Delete Guitar ---");
        String serial = InputHelper.getStringInput("Enter Serial Number to delete: ");
        System.out.print("Are you sure you want to delete this guitar? (yes/if NO press any other key): ");
        if (!InputHelper.getLine().trim().equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        boolean removed = inventory.removeGuitar(serial);
        if (removed) {
            InventoryFileManager.saveInventoryToFile(inventory);
            System.out.println("Guitar with Serial Number " + serial + " deleted.");
        } else {
            System.out.println("No guitar found with Serial Number " + serial + ".");
        }
    }

    public void addGuitar(Inventory inventory) {
        System.out.println("\n--- Add New Guitar ---");
        String serial;
        while (true) {
            serial = InputHelper.getStringInput("Serial Number");
            if (inventory.getGuitar(serial) != null) {
                System.out.println("A guitar with this serial number already exists. Please enter a unique serial number.");
            } else break;
        }

        double price = InputHelper.getDoubleInput("Price");
        Builder builder = InputHelper.getEnumInput("Builder", Builder.class);
        String model = InputHelper.getStringInput("Model");
        Type type = InputHelper.getEnumInput("Type", Type.class);
        int numStrings = InputHelper.getIntInput("Number of strings");
        Wood backWood = InputHelper.getEnumInput("Back Wood", Wood.class);
        Wood topWood = InputHelper.getEnumInput("Top Wood", Wood.class);

        inventory.addGuitar(serial, price, new GuitarSpec(builder, model, type, numStrings, backWood, topWood));
        InventoryFileManager.saveInventoryToFile(inventory);
        System.out.println("Guitar added successfully!");
    }

    public void advancedSearchGuitars(Inventory inventory) {
        List<Guitar> results = inventory.getAllGuitars();
        boolean searching = true;
        boolean filterApplied = false;

        while (searching && !results.isEmpty()) {
        	Printer.printSearchOptions();

            String input = InputHelper.getLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > 7) {
                    System.out.println("Invalid option. Try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    Builder builder = InputHelper.getEnumInput("Builder", Builder.class);
                    results.removeIf(g -> g.getSpec().getBuilder() != builder);
                    filterApplied = true;
                }
                case 2 -> {
                    String model = InputHelper.getStringInput("Model").toLowerCase();
                    results.removeIf(g -> !g.getSpec().getModel().equalsIgnoreCase(model));
                    filterApplied = true;
                }
                case 3 -> {
                    Type type = InputHelper.getEnumInput("Type", Type.class);
                    results.removeIf(g -> g.getSpec().getType() != type);
                    filterApplied = true;
                }
                case 4 -> {
                    int strings = InputHelper.getIntInput("Number of Strings");
                    results.removeIf(g -> g.getSpec().getNumStrings() != strings);
                    filterApplied = true;
                }
                case 5 -> {
                    Wood backWood = InputHelper.getEnumInput("Back Wood", Wood.class);
                    results.removeIf(g -> g.getSpec().getBackWood() != backWood);
                    filterApplied = true;
                }
                case 6 -> {
                    Wood topWood = InputHelper.getEnumInput("Top Wood", Wood.class);
                    results.removeIf(g -> g.getSpec().getTopWood() != topWood);
                    filterApplied = true;
                }
                case 7 -> {
                	if (!filterApplied) {
                        System.out.println("Search exited without applying any filters.");
                    }
                	searching = false;
                	return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }

            if (!results.isEmpty()) {
                System.out.println("\nMatching Guitars:");
                results.forEach(Printer::printGuitar);
            } else {
                System.out.println("No guitars match the criteria.");
            }
        }
    }
}

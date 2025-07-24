package com.aurionpro.GuitarApp.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class InventoryFileManager {
	private static final String FILE_NAME = "guitars.txt";

    public static void initializeInventoryFromFile(Inventory inventory) {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No existing file found. Adding default guitars...");
            initializeDefaultInventory(inventory);
            saveInventoryToFile(inventory);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 8) continue;
                String serial = parts[0];
                double price = Double.parseDouble(parts[1]);
                Builder builder = Builder.valueOf(parts[2]);
                String model = parts[3];
                Type type = Type.valueOf(parts[4]);
                int numStrings = Integer.parseInt(parts[5]);
                Wood backWood = Wood.valueOf(parts[6]);
                Wood topWood = Wood.valueOf(parts[7]);
                inventory.addGuitar(serial, price, new GuitarSpec(builder, model, type, numStrings, backWood, topWood));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static void saveInventoryToFile(Inventory inventory) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Guitar g : inventory.getAllGuitars()) {
                GuitarSpec s = g.getSpec();
                writer.println(String.join("|",
                        g.getSerialNumber(),
                        String.valueOf(g.getPrice()),
                        s.getBuilder().name(),
                        s.getModel(),
                        s.getType().name(),
                        String.valueOf(s.getNumStrings()),
                        s.getBackWood().name(),
                        s.getTopWood().name()));
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    private static void initializeDefaultInventory(Inventory inv) {
        inv.addGuitar("11277", 3999.95, new GuitarSpec(Builder.COLLINGS, "CJ", Type.ACOUSTIC, 6, Wood.INDIAN_ROSEWOOD, Wood.SITKA));
        inv.addGuitar("V95693", 1499.95, new GuitarSpec(Builder.FENDER, "Stratocaster", Type.ELECTRIC, 6, Wood.ALDER, Wood.ALDER));
        inv.addGuitar("V9512", 1549.95, new GuitarSpec(Builder.FENDER, "Stratocaster", Type.ELECTRIC, 6, Wood.ALDER, Wood.ALDER));
        inv.addGuitar("122784", 5495.95, new GuitarSpec(Builder.MARTIN, "D-18", Type.ACOUSTIC, 6, Wood.MAHOGANY, Wood.ADIRONDACK));
        inv.addGuitar("76531", 6295.95, new GuitarSpec(Builder.MARTIN, "OM-28", Type.ACOUSTIC, 6, Wood.BRAZILIAN_ROSEWOOD, Wood.ADIRONDACK));
        inv.addGuitar("70108276", 2295.95, new GuitarSpec(Builder.GIBSON, "Les Paul", Type.ELECTRIC, 6, Wood.MAHOGANY, Wood.MAPLE));
        inv.addGuitar("82765501", 1890.95, new GuitarSpec(Builder.GIBSON, "SG '61 Reissue", Type.ELECTRIC, 6, Wood.MAHOGANY, Wood.MAHOGANY));
        inv.addGuitar("77023", 6275.95, new GuitarSpec(Builder.MARTIN, "D-28", Type.ACOUSTIC, 6, Wood.BRAZILIAN_ROSEWOOD, Wood.ADIRONDACK));
        inv.addGuitar("1092", 12995.95, new GuitarSpec(Builder.OLSON, "SJ", Type.ACOUSTIC, 6, Wood.INDIAN_ROSEWOOD, Wood.CEDAR));
        inv.addGuitar("566-62", 8999.95, new GuitarSpec(Builder.RYAN, "Cathedral", Type.ACOUSTIC, 6, Wood.COCOBOLO, Wood.CEDAR));
        inv.addGuitar("6 29584", 2100.95, new GuitarSpec(Builder.PRS, "Dave Navarro Signature", Type.ELECTRIC, 6, Wood.MAHOGANY, Wood.MAPLE));
    }
}

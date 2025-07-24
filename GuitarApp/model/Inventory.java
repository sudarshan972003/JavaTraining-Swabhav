package com.aurionpro.GuitarApp.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Inventory {
	private List<Guitar> guitars;

    public Inventory() {
        guitars = new LinkedList<>();
    }

    public void addGuitar(String serialNumber, double price, GuitarSpec spec) {
        Guitar guitar = new Guitar(serialNumber, price, spec);
        guitars.add(guitar);
    }

    public Guitar getGuitar(String serialNumber) {
        for (Guitar guitar : guitars) {
            if (guitar.getSerialNumber().equals(serialNumber)) {
                return guitar;
            }
        }
        return null;
    }
    
    public boolean removeGuitar(String serialNumber) {
        return guitars.removeIf(g -> g.getSerialNumber().equalsIgnoreCase(serialNumber));
    }

    public List<Guitar> getAllGuitars() {
        return new ArrayList<>(guitars);
    }
}

package com.FoodOrderingConsoleApp.entity;

public class MenuItem {
	private int id;
    private String name;
    private double price;

    public MenuItem() {}

    public MenuItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Price: ₹%.2f", id, name, price);
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}

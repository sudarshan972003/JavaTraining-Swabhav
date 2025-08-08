package com.FoodOrderingConsoleApp.entity;

public class Coupon {
	private String code;
    private double discount;

    public Coupon() {}

    public Coupon(String code, double discount) {
        this.code = code;
        this.discount = discount;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

	@Override
	public String toString() {
		return "Coupon [code=" + code + ", discount=" + discount + "]";
	}
}

package com.aurionpro.EcommerceDomain.model;

public class CreditCard implements IPaymentGateway {
    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " via Credit Card.");
    }

    @Override
    public void refund(double amount) {
        System.out.println("Refunded ₹" + amount + " to Credit Card.");
    }
}

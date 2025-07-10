package com.aurionpro.EcommerceDomain.model;

public class NetBanking implements IPaymentGateway {
    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " via Net Banking.");
    }

    @Override
    public void refund(double amount) {
        System.out.println("Refunded ₹" + amount + " via Net Banking.");
    }
}

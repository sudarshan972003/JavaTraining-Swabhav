package com.aurionpro.EcommerceDomain.model;

public interface IPaymentGateway {

	void pay(double amount);
    void refund(double amount);
}

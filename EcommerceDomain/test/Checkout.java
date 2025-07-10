package com.aurionpro.EcommerceDomain.test;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.EcommerceDomain.model.CreditCard;
import com.aurionpro.EcommerceDomain.model.IPaymentGateway;
import com.aurionpro.EcommerceDomain.model.NetBanking;
import com.aurionpro.EcommerceDomain.model.UPI;

public class Checkout {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		boolean continueTransaction = true;

		while (continueTransaction) {
			double amount = 0;

			while (true) {
				try {
					System.out.print("Enter amount to pay: ₹");
					if (!scanner.hasNextDouble()) {
						System.out.println("Invalid input. Please enter a valid number.");
						scanner.next();
						continue;
					}

					amount = scanner.nextDouble();
					scanner.nextLine();

					if (amount <= 0) {
						System.out.println("Amount must be greater than zero.");
						continue;
					}
					break;
				} catch (InputMismatchException e) {
					System.out.println("Please enter numeric value.");
					scanner.nextLine();
				}
			}

			IPaymentGateway payment = null;
			while (true) {
				try {
					System.out.println("\nChoose Payment Method:");
					System.out.println("1. Credit Card");
					System.out.println("2. UPI");
					System.out.println("3. Net Banking");
					System.out.print("Enter choice (1-3): ");

					if (!scanner.hasNextInt()) {
						System.out.println("Invalid input. Please enter a number.");
						scanner.next();
						continue;
					}

					int choice = scanner.nextInt();
					scanner.nextLine();

					switch (choice) {
					case 1:
						payment = new CreditCard();
						break;
					case 2:
						payment = new UPI();
						break;
					case 3:
						payment = new NetBanking();
						break;
					default:
						System.out.println("Invalid choice. Please try again.");
						continue;
					}
					break;
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
					scanner.nextLine();
				}
			}

			payment.pay(amount);

			while (true) {
				System.out.print("Do you want to refund? (yes/no): ");
				String refundChoice = scanner.nextLine().trim();

				if (refundChoice.equalsIgnoreCase("yes")) {
					payment.refund(amount);
					break;
				} else if (refundChoice.equalsIgnoreCase("no")) {
					System.out.println("No refund initiated.");
					break;
				} else {
					System.out.println("Invalid input. Please enter 'yes' or 'no'.");
				}
			}

			while (true) {
				System.out.print("\nDo you want to perform another transaction? (yes/no): ");
				String again = scanner.nextLine().trim();

				if (again.equalsIgnoreCase("yes")) {
					System.out.println("\n----------------------------------\n");
					break;
				} else if (again.equalsIgnoreCase("no")) {
					continueTransaction = false;
					break;
				} else {
					System.out.println("Invalid input. Please enter 'yes' or 'no'.");
				}
			}
		}
		System.out.println("\nTransaction completed.");
		scanner.close();
	}
}

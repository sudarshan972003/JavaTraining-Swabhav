package com.aurionpro.BankingApplication.test;

import java.util.List;
import java.util.Scanner;

import com.aurionpro.BankingApplication.model.Account;
import com.aurionpro.BankingApplication.model.BankingFacade;
import com.aurionpro.BankingApplication.model.InputUtil;
import com.aurionpro.BankingApplication.model.PasswordUtil;
import com.aurionpro.BankingApplication.model.Transaction;

public class App {
	public static void main(String[] args) {
        BankingFacade facade = new BankingFacade();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Welcome to Secure Transaction Banking ===\n");
            System.out.println("1. Login");
            System.out.println("2. Create New Account");
            System.out.println("3. Exit");
            int choice = InputUtil.readIntInRange("Enter choice: ", 1, 3);

            try {
                if (choice == 1) {
                    String userId = InputUtil.readValidUserId("Enter User ID: ");
                    List<Account> accounts = facade.getActiveAccounts(userId);
                    if (accounts.isEmpty()) {
                        System.out.println("No active accounts found for this User ID.");
                        continue;
                    }

                    System.out.println("Select Account:");
                    for (int i = 0; i < accounts.size(); i++) {
                        System.out.println((i + 1) + ". " + accounts.get(i).getAccountNumber());
                    }
                    int accChoice = InputUtil.readIntInRange("Choose account: ", 1, accounts.size());
                    Account currentAccount = accounts.get(accChoice - 1);

                    String password = InputUtil.readMaskedPassword("Enter Password: ");
                    if (!facade.validatePassword(currentAccount, password)) {
                        System.out.println("Incorrect password.");
                        continue;
                    }

                    while (true) {
                        System.out.println("\n--- Logged In as: " + currentAccount.getName() + " ---");
                        System.out.println("1. Check Balance");
                        System.out.println("2. Deposit");
                        System.out.println("3. Withdraw");
                        System.out.println("4. Transfer Money");
                        System.out.println("5. Transaction History");
                        System.out.println("6. Profile & Account Settings");
                        System.out.println("7. Logout");

                        int op = InputUtil.readIntInRange("Choose option: ", 1, 7);

                        try {
                        switch (op) {
                            case 1:
                                System.out.printf("Balance: ₹%.2f%n", currentAccount.getBalance());
                                break;
                            case 2:
                                int dep = InputUtil.readPositiveMultipleOf10("Enter deposit amount: ");
                                facade.deposit(currentAccount, dep);
                                System.out.println("Deposit successful.");
                                break;
                            case 3:
                                int wd = InputUtil.readPositiveMultipleOf10("Enter withdrawal amount: ");
                                facade.withdraw(currentAccount, wd);
                                System.out.println("Withdrawal successful.");
                                break;
                            case 4:
                                String targetAccNum = InputUtil.readNonEmpty("Enter receiver account number: ");
                                if (targetAccNum.equals(currentAccount.getAccountNumber())) {
                                    System.out.println("Cannot transfer to same account.");
                                    break;
                                }
                                Account receiver = facade.getAccount(targetAccNum);
                                double transferAmount = InputUtil.readPositiveDouble("Enter amount: ");
                                facade.transfer(currentAccount, receiver, transferAmount);
                                System.out.println("Transfer successful.");
                                break;
                            case 5:
                                List<Transaction> history = facade.getTransactionHistory(currentAccount.getAccountNumber());
                                System.out.println("\n-- Transaction History --");
                                System.out.printf("%-6s %-20s %-13s %-12s %-15s %-15s%n",
                                        "ID", "Created At", "Type", "Amount", "BalanceAfter", "Counterparty");

                                for (Transaction t : history) {
                                    System.out.printf("%-6d %-20s %-13s %-12.2f %-15.2f %-15s%n",
                                            t.getId(),
                                            t.getCreatedAt().toString(),
                                            t.getType(),
                                            (double) t.getAmount(),
                                            t.getBalanceAfter(),
                                            t.getCounterparty());
                                }
                                break;
                            case 6:
                                System.out.println("\n--- Profile Settings ---");
                                System.out.println("1. View Details");
                                System.out.println("2. Change Password");
                                System.out.println("3. Update Name");
                                System.out.println("4. Close Account");
                                System.out.println("5. Exit");
                                int p = InputUtil.readIntInRange("Option: ", 1, 5);
                                switch (p) {
                                    case 1:
                                        System.out.printf("Account: %s, Name: %s, Balance: ₹%.2f%n",
                                                currentAccount.getAccountNumber(), currentAccount.getName(), currentAccount.getBalance());
                                        break;
                                    case 2:
                                        String newPass = InputUtil.readMaskedPassword("New password: ");
                                        String confPass = InputUtil.readMaskedPassword("Confirm password: ");
                                        if (!newPass.equals(confPass)) {
                                            System.out.println("Passwords do not match.");
                                            break;
                                        }
                                        currentAccount.setPassword(PasswordUtil.hashPassword(newPass));
                                        facade.updateAccount(currentAccount);
                                        System.out.println("Password changed.");
                                        break;
                                    case 3:
                                        String newName = InputUtil.readNonEmpty("Enter new name: ");
                                        if (!newName.matches("^[a-zA-Z ]+$")) {
                                            System.out.println("Invalid name.");
                                            break;
                                        }
                                        currentAccount.setName(newName);
                                        facade.updateAccount(currentAccount);
                                        System.out.println("Name updated.");
                                        break;
                                    case 4:
                                        if (InputUtil.getConfirmation("Are you sure to close account? ")) {
                                            currentAccount.setActive(false);
                                            facade.updateAccount(currentAccount);
                                            System.out.println("Account closed. Logging out.");
                                            p = 5;
                                            op = 7;
                                        }
                                        break;
                                    case 5:
                                        break;
                                }
                                break;
                            case 7:
                                System.out.println("Logged out.\n");
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                        if (op == 7) break;
                    }

                } else if (choice == 2) {
                    String userId = InputUtil.readValidUserId("Enter User ID: ");
                    String name = InputUtil.readNonEmpty("Enter Name: ");
                    if (!name.matches("^[a-zA-Z ]+$")) {
                        System.out.println("Name must not contain digits or symbols.");
                        continue;
                    }

                    String password = InputUtil.readMaskedPassword("Enter Password: ");
                    String confirmPassword = InputUtil.readMaskedPassword("Confirm Password: ");
                    if (!password.equals(confirmPassword)) {
                        System.out.println("Passwords do not match.");
                        continue;
                    }

                    double balance = InputUtil.readPositiveDouble("Enter Initial Balance: ");
                    Account newAcc = facade.createAccount(userId, name, password, balance);
                    System.out.println("Account created successfully. Account Number: " + newAcc.getAccountNumber());

                } else {
                    System.out.println("Thank you for banking with us!");
                    break;
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        sc.close();
    }
}

package com.FoodOrderingConsoleApp.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.FoodOrderingConsoleApp.DBOperation.CouponDAO;
import com.FoodOrderingConsoleApp.DBOperation.DeliveryPartnerDAO;
import com.FoodOrderingConsoleApp.DBOperation.MenuDAO;
import com.FoodOrderingConsoleApp.entity.CartItem;
import com.FoodOrderingConsoleApp.entity.Coupon;
import com.FoodOrderingConsoleApp.entity.DeliveryPartner;
import com.FoodOrderingConsoleApp.entity.MenuItem;
import com.FoodOrderingConsoleApp.entity.User;

public class OrderService {
    private final MenuDAO menuDAO = new MenuDAO();
    private final CouponDAO couponDAO = new CouponDAO();
    private final DeliveryPartnerDAO deliveryDAO = new DeliveryPartnerDAO();

    private final List<CartItem> cart = new ArrayList<>();
    private Coupon appliedCoupon = null; 

    public void orderMenu(User user) throws ClassNotFoundException {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View Menu and Add to Cart");
            System.out.println("2. Cart Menu");
            System.out.println("3. Logout");
            int choice = InputUtil.getInt("Enter choice: ");

            switch (choice) {
                case 1 -> showAndAddMenu();
                case 2 -> cartMenu(user);
                case 3 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void showAndAddMenu() throws ClassNotFoundException {
        List<MenuItem> items = menuDAO.findAll();
        if (items.isEmpty()) {
            System.out.println("Menu is empty.");
            return;
        }

        boolean continueAdding = true;
        while (continueAdding) {
            System.out.println("\n--- Menu ---");
            for (MenuItem item : items) {
                System.out.println(item.getId() + ". " + item.getName() + " - ₹" + item.getPrice());
            }

            int id = InputUtil.getInt("Enter item ID to add to cart: ");
            int qty = InputUtil.getInt("Enter quantity: ");

            Optional<MenuItem> found = items.stream().filter(i -> i.getId() == id).findFirst();
            if (found.isPresent()) {
                cart.add(new CartItem(found.get(), qty));
                System.out.println("Added to cart.");
            } else {
                System.out.println("Invalid item ID.");
            }

            String choice = InputUtil.getString("Do you want to add another item? (y/n): ").trim().toLowerCase();
            if (!choice.equals("y")) {
                continueAdding = false;
            }
        }
    }

    private void cartMenu(User user) throws ClassNotFoundException {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println("\n--- Cart Menu ---");
            System.out.println("1. View Cart");
            System.out.println("2. Order Now");
            System.out.println("3. Empty Cart");
            System.out.println("4. Go Back");
            int choice = InputUtil.getInt("Choose an option: ");

            switch (choice) {
                case 1 -> viewCart();
                case 2 -> {
                    if (!cart.isEmpty()) {
                        orderCart(user);
                        running = false;
                    } else {
                        System.out.println("Your cart is empty!");
                    }
                }
                case 3 -> {
                    cart.clear();
                    appliedCoupon = null;
                    System.out.println("Cart has been emptied.");
                }
                case 4 -> running = false;
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    private void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("\n--- Cart ---");
        for (CartItem item : cart) {
            System.out.println(item.getItem().getName() + " x " + item.getQuantity() + " = ₹" + item.getTotal());
        }
    }

    private void orderCart(User user) throws ClassNotFoundException {
        double total = cart.stream().mapToDouble(CartItem::getTotal).sum();

        System.out.println("\n--- Cart ---");
        cart.forEach(item ->
            System.out.println(item.getItem().getName() + " x " + item.getQuantity() + " = ₹" + item.getTotal())
        );

        double discount = 0;

        if (total > 500) {
            double percentDiscount = total * 0.10;
            discount += percentDiscount;
            System.out.println("10% discount applied: ₹" + percentDiscount);
        }

        String apply = InputUtil.getString("Apply coupon? (yes/no): ");
        if (apply.equalsIgnoreCase("yes")) {
            String code = InputUtil.getString("Enter coupon code: ");
            appliedCoupon = couponDAO.findByCode(code);
            if (appliedCoupon != null) {
                discount += appliedCoupon.getDiscount();
                System.out.println("Coupon applied: ₹" + appliedCoupon.getDiscount());
            } else {
                System.out.println("Invalid coupon.");
            }
        }

        double grandTotal = total - discount;
        System.out.println("Total: ₹" + total);
        System.out.println("Discount: ₹" + discount);
        System.out.println("Grand Total: ₹" + grandTotal);

        String proceed = InputUtil.getString("Proceed to payment? (yes/no): ");
        if (proceed.equalsIgnoreCase("yes")) {
            processPayment(total, discount, user);
            cart.clear();
            appliedCoupon = null;
        }
    }

    private void processPayment(double total, double discount, User user) throws ClassNotFoundException {
        System.out.println("\n--- Payment ---");
        System.out.println("1. Cash");
        System.out.println("2. UPI");
        int mode = InputUtil.getInt("Select payment method: ");
        String paymentMode = switch (mode) {
            case 1 -> "Cash";
            case 2 -> "UPI";
            default -> "Unknown";
        };

        List<DeliveryPartner> partners = deliveryDAO.findAll();
        if (partners.isEmpty()) {
            System.out.println("No delivery partners available!");
            return;
        }
        DeliveryPartner assigned = partners.get(new Random().nextInt(partners.size()));

        System.out.println("\n========= INVOICE =========");
        System.out.println("Customer: " + user.getFirstName() + " " + user.getLastName());
        System.out.println("Mobile: " + user.getMobileNo());
        System.out.println("Email: " + user.getEmailId());
        System.out.println("Address: " + user.getAddress());
        System.out.println("\nItems:");
        cart.forEach(item ->
            System.out.println("- " + item.getItem().getName() + " x " + item.getQuantity() + " = ₹" + item.getTotal())
        );

        System.out.println("Subtotal: ₹" + total);
        System.out.println("Discount: ₹" + discount);
        System.out.println("Total Payable: ₹" + (total - discount));
        if (appliedCoupon != null) {
            System.out.println("Applied Coupon: " + appliedCoupon.getCode());
        }
        System.out.println("Payment Mode: " + paymentMode);
        System.out.println("Delivery Partner: " + assigned.getName());
        System.out.println("============================");
    }

}

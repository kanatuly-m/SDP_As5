package ecommerce;
import ecommerce.core.*;
import ecommerce.payments.*;
import ecommerce.facade.CheckoutFacade;
import java.math.BigDecimal;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        OrderBuilder ob = new OrderBuilder();
        CheckoutFacade facade = new CheckoutFacade();

        boolean enableFraud = false;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal cashback = BigDecimal.ZERO;
        int payChoice = 1;

        while (true){
            System.out.println("""
            
            1) Add item
            2) Set customer
            3) Delivery: Pickup/Standard/Express
            4) Discount %
            5) Cashback %
            6) Fraud check on/off
            7) Payment: 1) CreditCard  2) PayPal
            8) Checkout
            9) Exit
            10) Reset cart
            Choose:""");
            String s = in.nextLine().trim();
            switch (s){
                case "1" -> {
                    System.out.print("ID: "); String sku = in.nextLine().trim();
                    System.out.print("Title: "); String title = in.nextLine().trim();
                    System.out.print("Qty: "); int qty = Integer.parseInt(in.nextLine().trim());
                    System.out.print("Price: "); BigDecimal price = new BigDecimal(in.nextLine().trim());
                    ob.addItem(sku, title, qty, price);
                    System.out.println("Added.");
                }
                case "2" -> {
                    System.out.print("Customer ID: "); String id = in.nextLine().trim();
                    System.out.print("Name: "); String name = in.nextLine().trim();
                    System.out.print("Email: "); String email = in.nextLine().trim();
                    ob.setCustomer(id, name, email);
                    System.out.println("Customer set.");
                }
                case "3" -> {
                    System.out.print("Delivery (Pickup/Standard/Express): ");
                    String d = in.nextLine().trim();
                    try {
                        ob.setDelivery(d);
                        System.out.println("Delivery set: " + d);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case "4" -> {
                    System.out.print("Discount percent (e.g., 10 for 10%): ");
                    try {
                        discount = new BigDecimal(in.nextLine().trim()).movePointLeft(2);
                        System.out.println("Discount set.");
                    } catch (Exception e) {
                        System.out.println("Error: invalid percent");
                    }
                }
                case "5" -> {
                    System.out.print("Cashback percent (e.g., 5 for 5%): ");
                    try {
                        cashback = new BigDecimal(in.nextLine().trim()).movePointLeft(2);
                        System.out.println("Cashback set.");
                    } catch (Exception e) {
                        System.out.println("Error: invalid percent");
                    }
                }
                case "6" -> {
                    enableFraud = !enableFraud;
                    System.out.println("Fraud check: " + (enableFraud ? "ON" : "OFF"));
                }
                case "7" -> {
                    System.out.print("Choose 1=CreditCard, 2=PayPal: ");
                    try { payChoice = Integer.parseInt(in.nextLine().trim()); }
                    catch (Exception ignored) {}
                }
                case "8" -> {
                    try {
                        Order order = ob.build();
                        var base = (payChoice == 1) ? new CreditCardPayment() : new PayPalPayment();
                        var receipt = facade.processOrder(order, base, enableFraud, discount, cashback);
                        System.out.println("=== RECEIPT ===");
                        System.out.println(receipt);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case "9" -> { return;
                }
                case "10" -> {
                    ob = new OrderBuilder();
                    discount = BigDecimal.ZERO;
                    cashback = BigDecimal.ZERO;
                    enableFraud = false;
                    System.out.println("Cart and discounts reset.");
                }
                default -> System.out.println("Unknown option.");
            }
        }
    }
}

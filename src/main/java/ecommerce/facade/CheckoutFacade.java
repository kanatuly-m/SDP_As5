package ecommerce.facade;
import ecommerce.core.*;
import ecommerce.decorator.*;
import ecommerce.payments.*;
import java.math.BigDecimal;
import java.util.Scanner;

public class CheckoutFacade{
    private OrderBuilder orderBuilder;
    private boolean enableFraud = false;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal cashback = BigDecimal.ZERO;
    private int payChoice = 1;

    public CheckoutFacade(){
        this.orderBuilder = new OrderBuilder();
    }

    public void startCheckout() {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("""
                SmartShop
            1) Add item
            2) Set customer
            3) Delivery: Pickup/Standard/Express
            4) Discount (0-10)%
            5) Cashback (0-5)%
            6) Fraud check on/off
            7) Payment: 1) CreditCard  2) PayPal
            8) Checkout
            9) Exit
            10) Reset cart
            Choose:""");

            String s = in.nextLine().trim();
            switch (s) {
                case "1" -> {
                    System.out.print("ID: ");
                    String sku = in.nextLine().trim();
                    System.out.print("Title: ");
                    String title = in.nextLine().trim();
                    System.out.print("Qty: ");
                    int qty = Integer.parseInt(in.nextLine().trim());
                    System.out.print("Price: ");
                    BigDecimal price = new BigDecimal(in.nextLine().trim());
                    orderBuilder.addItem(sku, title, qty, price);
                    System.out.println("Added.");
                }
                case "2" -> {
                    System.out.print("Customer ID: ");
                    String id = in.nextLine().trim();
                    System.out.print("Name: ");
                    String name = in.nextLine().trim();
                    System.out.print("Email: ");
                    String email = in.nextLine().trim();
                    orderBuilder.setCustomer(id, name, email);
                    System.out.println("Customer set.");
                }
                case "3" -> {
                    System.out.print("Delivery (Pickup/Standard/Express): ");
                    String d = in.nextLine().trim();
                    try {
                        orderBuilder.setDelivery(d);
                        System.out.println("Delivery set: " + d);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case "4" -> {
                    System.out.print("Discount percent (0-10%): ");
                    try {
                        discount = new BigDecimal(in.nextLine().trim()).movePointLeft(2);
                        System.out.println("Discount set.");
                    } catch (Exception e) {
                        System.out.println("Error: invalid percent");
                    }
                }
                case "5" -> {
                    System.out.print("Cashback percent (0-5%): ");
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
                        Order order = orderBuilder.build();
                        var base = (payChoice == 1) ? new CreditCardPayment() : new PayPalPayment();
                        var receipt = this.processOrder(order, base, enableFraud, discount, cashback);
                        System.out.println("    RECEIPT   ");
                        System.out.println(receipt);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case "9" -> { return; }
                case "10" -> {
                    orderBuilder = new OrderBuilder();
                    discount = BigDecimal.ZERO;
                    cashback = BigDecimal.ZERO;
                    enableFraud = false;
                    System.out.println("Cart and discounts reset.");
                }
                default -> System.out.println("Unknown option.");
            }
        }
    }
    public Receipt processOrder(Order order,
                                Payment basePayment,
                                boolean enableFraud,
                                BigDecimal discountPct,
                                BigDecimal cashbackPct){
        Payment p = basePayment;
        if (discountPct != null && discountPct.signum() > 0) p = new DiscountDecorator(p, discountPct);
        if (cashbackPct != null && cashbackPct.signum() > 0) p = new CashbackDecorator(p, cashbackPct);
        if (enableFraud) p = new FraudDetectionDecorator(p);
        return p.pay(order);
    }
}

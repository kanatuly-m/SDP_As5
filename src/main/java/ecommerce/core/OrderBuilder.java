package ecommerce.core;
import java.math.BigDecimal;
import java.util.*;

public class OrderBuilder {
    private String id = UUID.randomUUID().toString();
    private Customer customer = new Customer("anon", "Guest", "guest@example.com");
    private final List<CartItem> items = new ArrayList<>();
    private String delivery = "Pickup"; // Pickup | Standard | Express

    public OrderBuilder setId(String id) {
        this.id = Objects.requireNonNull(id);
        return this;
    }
    public OrderBuilder setCustomer(String id, String name, String email){
        this.customer = new Customer(id, name, email);
        return this;
    }
    public OrderBuilder addItem(String sku, String title, int qty, BigDecimal price){
        if (qty<=0) throw new IllegalArgumentException("qty>0 required");
        if (price.signum()<0) throw new IllegalArgumentException("price>=0 required");
        items.add(new CartItem(sku, title, qty, price));
        return this;
    }
    public OrderBuilder setDelivery(String method){
        this.delivery = switch (method.toLowerCase()){
            case "pickup" -> "Pickup";
            case "standard" -> "Standard";
            case "express" -> "Express";
            default -> throw new IllegalArgumentException("Unknown delivery: " + method);
        };
        return this;
    }

    public Order build(){
        if (items.isEmpty()) throw new IllegalStateException("Cart is empty");
        return new Order(id, customer, List.copyOf(items), delivery);
    }
}
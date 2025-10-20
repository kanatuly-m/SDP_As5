package ecommerce.core;
import java.math.BigDecimal;
import java.util.List;

public record Order(String id, Customer customer, List<CartItem> items, String deliveryMethod){
    public BigDecimal subtotal(){
        return items.stream().map(CartItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
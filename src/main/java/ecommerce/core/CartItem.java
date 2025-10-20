package ecommerce.core;
import java.math.BigDecimal;

public record CartItem(String sku, String title, int qty, BigDecimal price){
    public BigDecimal lineTotal(){
        return price.multiply(BigDecimal.valueOf(qty));
    }
}
package ecommerce.payments;
import ecommerce.core.*;
import java.math.BigDecimal;
import java.time.Instant;

public class PayPalPayment implements Payment{
    @Override
    public Receipt pay(Order order){
        BigDecimal amount = order.subtotal();
        return new ReceiptBuilder()
                .orderId(order.id())
                .method("PayPal")
                .charged(amount)
                .notes("PayPal ok")
                .at(Instant.now())
                .build();
    }
}
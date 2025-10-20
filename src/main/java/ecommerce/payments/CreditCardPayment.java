package ecommerce.payments;
import ecommerce.core.*;
import java.math.BigDecimal;
import java.time.Instant;

public class CreditCardPayment implements Payment{
    @Override
    public Receipt pay(Order order){
        BigDecimal amount = order.subtotal();
        return new ReceiptBuilder()
                .orderId(order.id())
                .method("CreditCard")
                .charged(amount)
                .notes("CC ok")
                .at(Instant.now())
                .build();
    }
}
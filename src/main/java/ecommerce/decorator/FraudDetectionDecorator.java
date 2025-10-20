package ecommerce.decorator;
import ecommerce.core.*;

public class FraudDetectionDecorator extends PaymentDecorator{
    public FraudDetectionDecorator(Payment inner) { super(inner);
    }

    @Override
    public Receipt pay(Order order){
        boolean suspicious = order.subtotal().doubleValue() > 1000.0 || order.items().size() > 10;
        Receipt r = inner.pay(order);
        String tag = suspicious ? " | fraud: review" : " | fraud: clear";
        return new ReceiptBuilder()
                .orderId(r.orderId())
                .method(r.paymentMethod())
                .charged(r.charged())
                .notes(r.notes() + tag)
                .at(r.timestamp())
                .build();
    }
}
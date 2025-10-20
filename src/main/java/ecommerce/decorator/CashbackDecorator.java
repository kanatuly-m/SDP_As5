package ecommerce.decorator;
import ecommerce.core.*;
import java.math.BigDecimal;

public class CashbackDecorator extends PaymentDecorator{
    private final BigDecimal percent;
    public CashbackDecorator(Payment inner, BigDecimal percent){
        super(inner);
        if (percent == null || percent.signum() < 0) throw new IllegalArgumentException("percent >= 0");
        this.percent = percent;
    }

    @Override
    public Receipt pay(Order order){
        Receipt r = inner.pay(order);
        BigDecimal points = r.charged().multiply(percent);
        String tag = " | cashback " + points.stripTrailingZeros().toPlainString();
        return new ReceiptBuilder()
                .orderId(r.orderId())
                .method(r.paymentMethod())
                .charged(r.charged())
                .notes(r.notes() + tag)
                .at(r.timestamp())
                .build();
    }
}
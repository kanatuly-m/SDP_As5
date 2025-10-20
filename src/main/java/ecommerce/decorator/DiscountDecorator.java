package ecommerce.decorator;
import ecommerce.core.*;
import java.math.BigDecimal;

public class DiscountDecorator extends PaymentDecorator{
    private final BigDecimal percent;

    public DiscountDecorator(Payment inner, BigDecimal percent){
        super(inner);
        if (percent == null || percent.signum() < 0) throw new IllegalArgumentException("percent >= 0");
        this.percent = percent;
    }

    @Override
    public Receipt pay(Order order){
        BigDecimal base = order.subtotal();
        BigDecimal discounted = base.subtract(base.multiply(percent)).max(BigDecimal.ZERO);

        Receipt r = inner.pay(order);

        String tag = " | discount " + percent.movePointRight(2).stripTrailingZeros().toPlainString() + "%";
        return new ReceiptBuilder()
                .orderId(r.orderId())
                .method(r.paymentMethod())
                .charged(discounted)
                .notes(r.notes() + tag)
                .at(r.timestamp())
                .build();
    }
}

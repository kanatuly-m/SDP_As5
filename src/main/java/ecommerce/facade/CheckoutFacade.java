package ecommerce.facade;
import ecommerce.core.*;
import ecommerce.decorator.*;
import java.math.BigDecimal;

public class CheckoutFacade {
    public Receipt processOrder(Order order,
                                Payment basePayment,
                                boolean enableFraud,
                                BigDecimal discountPct,
                                BigDecimal cashbackPct) {
        Payment p = basePayment;
        if (discountPct != null && discountPct.signum() > 0) p = new DiscountDecorator(p, discountPct);
        if (cashbackPct != null && cashbackPct.signum() > 0) p = new CashbackDecorator(p, cashbackPct);
        if (enableFraud) p = new FraudDetectionDecorator(p);
        return p.pay(order);
    }
}
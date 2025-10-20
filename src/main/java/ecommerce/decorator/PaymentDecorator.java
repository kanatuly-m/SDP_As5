package ecommerce.decorator;
import ecommerce.core.*;

public abstract class PaymentDecorator implements Payment{
    protected final Payment inner;
    protected PaymentDecorator(Payment inner){
        this.inner = inner;
    }
}
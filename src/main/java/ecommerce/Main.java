package ecommerce;
import ecommerce.facade.CheckoutFacade;

public class Main {
    public static void main(String[] args){
        CheckoutFacade checkoutFacade = new CheckoutFacade();
        checkoutFacade.startCheckout();
    }
}

package ecommerce.core;

public interface Payment{
    Receipt pay(Order order);
}

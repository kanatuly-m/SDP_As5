package ecommerce.core;
import java.math.BigDecimal;
import java.time.Instant;

public class ReceiptBuilder{
    private String orderId;
    private String paymentMethod;
    private BigDecimal charged = BigDecimal.ZERO;
    private String notes = "";
    private Instant timestamp = Instant.now();

    public ReceiptBuilder orderId(String v){
        this.orderId = v; return this;
    }
    public ReceiptBuilder method(String v){
        this.paymentMethod = v; return this;
    }
    public ReceiptBuilder charged(BigDecimal v){
        this.charged = v; return this;
    }
    public ReceiptBuilder notes(String v){
        this.notes = v; return this;
    }
    public ReceiptBuilder at(Instant t){
        this.timestamp = t; return this;
    }

    public Receipt build(){
        return new Receipt(orderId, paymentMethod, charged, notes, timestamp);
    }
}
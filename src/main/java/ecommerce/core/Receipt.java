package ecommerce.core;
import java.math.BigDecimal;
import java.time.Instant;

public record Receipt(
        String orderId,
        String paymentMethod,
        BigDecimal charged,
        String notes,
        Instant timestamp
){}
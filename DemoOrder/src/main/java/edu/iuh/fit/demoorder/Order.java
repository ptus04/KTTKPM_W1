package edu.iuh.fit.demoorder;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class Order {
    private String orderId;
    private String customerName;
    private LocalDateTime orderDate;
}

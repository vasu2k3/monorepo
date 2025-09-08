package com.example.ordersservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(
    name = "orders"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", columnDefinition = "BIGINT")
    private Long orderId;

  
    @Column(name = "order_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime orderDate;
    
    @Column(name = "order_customer_id", columnDefinition = "INT")
    private Integer orderCustomerId;


    @Column(name = "order_status", columnDefinition = "VARCHAR(255)")
    private String orderStatus;
}

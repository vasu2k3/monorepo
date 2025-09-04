package com.example.ordersservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;   // matches LongType

    private LocalDateTime orderDate; // matches TimestampType

    private Integer orderCustomerId; // matches IntegerType

    private String orderStatus;      // matches StringType
}

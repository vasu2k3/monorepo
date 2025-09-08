package com.example.ordersservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId; // Primary Key

    @Column(nullable = false)
    private String firstName; // Customer first name

    @Column(nullable = false)
    private String lastName; // Customer last name

    @Column(nullable = false, unique = true)
    private String email; // Unique email for login or identification

    @Column(nullable = false)
    private String phone; // Phone number

    @Column(nullable = false)
    private String address; // Customer address
}

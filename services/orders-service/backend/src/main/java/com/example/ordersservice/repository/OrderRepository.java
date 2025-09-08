package com.example.ordersservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ordersservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByOrderCustomerId(Integer customerId);
	
}

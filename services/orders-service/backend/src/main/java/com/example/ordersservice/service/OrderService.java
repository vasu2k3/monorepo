package com.example.ordersservice.service;

import com.example.ordersservice.model.Order;
import com.example.ordersservice.repository.OrderRepository;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id).map(order -> {
            order.setOrderDate(updatedOrder.getOrderDate());
            order.setOrderCustomerId(updatedOrder.getOrderCustomerId());
            order.setOrderStatus(updatedOrder.getOrderStatus());
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
    public List<Order> getOrdersByCustomerId(Integer customerId) {
        return orderRepository.findByOrderCustomerId(customerId);
    }
    
}

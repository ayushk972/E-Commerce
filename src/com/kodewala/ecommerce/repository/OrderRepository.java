package com.kodewala.ecommerce.repository;

import com.kodewala.ecommerce.exception.InvalidOrderException;
import com.kodewala.ecommerce.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRepository {

    private final List<Order> orders = new ArrayList<>();

    // Save order
    public void saveOrder(Order order) {
        orders.add(order);
    }

    // Find order by ID
    public Order findById(String orderId) {
        return orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new InvalidOrderException(
                        "InvalidOrderException: Order with ID " + orderId + " does not exist."));
    }

    // Get all orders for a customer
    public List<Order> findByCustomerId(int customerId) {
        return orders.stream()
                .filter(o -> o.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    // Get all orders (for admin)
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }
}

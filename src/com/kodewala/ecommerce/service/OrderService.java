package com.kodewala.ecommerce.service;

import com.kodewala.ecommerce.exception.InvalidOrderException;
import com.kodewala.ecommerce.model.CartItem;
import com.kodewala.ecommerce.model.Order;
import com.kodewala.ecommerce.model.Product;
import com.kodewala.ecommerce.repository.OrderRepository;
import com.kodewala.ecommerce.model.OrderStatus;

import java.util.List;
import java.util.UUID;

public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, CartService cartService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    public String placeOrder(int customerId) {
        List<CartItem> cartItems = cartService.getCartItems(customerId);

        if (cartItems.isEmpty()) {
            throw new InvalidOrderException("InvalidOrderException: Cannot place order. Cart is empty.");
        }

        for (CartItem item : cartItems) {
            Product product = productService.getProduct(item.getProductId());
            if (product.getQuantity() < item.getQuantity()) {
                throw new InvalidOrderException(
                    "InvalidOrderException: Insufficient stock for '" + item.getProductName() +
                    "'. Available: " + product.getQuantity() + ", Requested: " + item.getQuantity());
            }
        }

       
        for (CartItem item : cartItems) {
            Product product = productService.getProduct(item.getProductId());
            product.setQuantity(product.getQuantity() - item.getQuantity());
        }

        double total = cartService.calculateTotal(customerId);
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Order order = new Order(orderId, customerId, cartItems, total);
        orderRepository.saveOrder(order);

      
        cartService.clearCart(customerId);

        System.out.println("\n   Order placed successfully!");
        System.out.println("  Order ID: " + orderId);
        System.out.printf("  Total qty: ₹%.2f%n", total);
        return orderId;
    }

    // View order details
    public void viewOrder(String orderId) {
        Order order = orderRepository.findById(orderId);
        System.out.println(order);
    }

    // View all orders of a customer
    public void viewCustomerOrders(int customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if (orders.isEmpty()) {
            System.out.println("  You have no orders yet.");
            return;
        }
        System.out.println("\n  ===== YOUR ORDERS (" + orders.size() + ") =====");
        orders.forEach(System.out::println);
    }

    // Cancel an order
    public void cancelOrder(String orderId, int customerId) {
        Order order = orderRepository.findById(orderId);

        if (order.getCustomerId() != customerId) {
            throw new InvalidOrderException("InvalidOrderException: You are not authorized to cancel order " + orderId);
        }
        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderException("InvalidOrderException: Cannot cancel a delivered order.");
        }
        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderException("InvalidOrderException: Order is already cancelled.");
        }

        // Restore inventory
        for (CartItem item : order.getItems()) {
            Product product = productService.getProduct(item.getProductId());
            product.setQuantity(product.getQuantity() + item.getQuantity());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        System.out.println("  Order [" + orderId + "] has been cancelled. Inventory restored.");
    }

    // View all orders
    public void viewAllOrders() {
        List<Order> orders = orderRepository.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("  No orders placed yet.");
            return;
        }
        System.out.println("\n  ===== ALL ORDERS (" + orders.size() + ") =====");
        orders.forEach(System.out::println);
    }

    
    public void updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId);
        OrderStatus current = order.getOrderStatus();

        // Cannot change status of a cancelled order
        if (current == OrderStatus.CANCELLED) {
            throw new InvalidOrderException(
                "InvalidOrderException: Cannot update a CANCELLED order.");
        }

        // Cannot move status backwards
        if (newStatus.ordinal() <= current.ordinal()) {
            throw new InvalidOrderException(
                "InvalidOrderException: Cannot move status from " + current +
                " to " + newStatus + ". Status can only move forward.");
        }

        order.setOrderStatus(newStatus);
        System.out.println("  Order [" + orderId + "] status updated: "
                + current + " → " + newStatus);
    }
}

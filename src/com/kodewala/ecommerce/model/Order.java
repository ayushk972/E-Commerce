package com.kodewala.ecommerce.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {

    private String orderId;
    private int customerId;
    private List<CartItem> items;
    private double totalAmount;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;

    public Order(String orderId, int customerId, List<CartItem> items, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderStatus = OrderStatus.PLACED;
        this.orderDate = LocalDateTime.now();
    }

    // Getters
    public String getOrderId()          { return orderId; }
    public int getCustomerId()          { return customerId; }
    public List<CartItem> getItems()    { return items; }
    public double getTotalAmount()      { return totalAmount; }
    public OrderStatus getOrderStatus() { return orderStatus; }
    public LocalDateTime getOrderDate() { return orderDate; }

    // Setter
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
            "+--------------------------------------------------+%n" +
            "| Order ID     : %-33s|%n" +
            "| Customer ID  : %-33d|%n" +
            "| Date         : %-33s|%n" +
            "| Status       : %-33s|%n" +
            "| Total Amount : ₹%-32.2f|%n" +
            "+------ Items ----------------------------------------+%n",
            orderId, customerId, orderDate.format(fmt), orderStatus, totalAmount
        ));
        for (CartItem item : items) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("+--------------------------------------------------+");
        return sb.toString();
    }
}

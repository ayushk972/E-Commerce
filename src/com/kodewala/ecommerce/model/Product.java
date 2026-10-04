package com.kodewala.ecommerce.model;

public class Product {
    private int productId;
    private String productName;
    private String category;
    private double price;
    private int quantity;
    private String brand;

    public Product(int productId, String productName, String category, double price, int quantity, String brand) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.brand = brand;
    }

    // Getters
	public int getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public String getCategory() {
		return category;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getBrand() {
		return brand;
	}

    // Setters
	public void setPrice(double price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

    @Override
    public String toString() {
        return String.format(
            "+--------------------------------------------------+%n" +
            "| Product ID   : %-33d|%n" +
            "| Name         : %-33s|%n" +
            "| Category     : %-33s|%n" +
            "| Brand        : %-33s|%n" +
            "| Price        : ₹%-32.2f|%n" +
            "| Quantity     : %-33d|%n" +
            "+--------------------------------------------------+",
            productId, productName, category, brand, price, quantity
        );
    }
}

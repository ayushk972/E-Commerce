package com.kodewala.ecommerce.service;

import com.kodewala.ecommerce.exception.InvalidQuantityException;
import com.kodewala.ecommerce.model.Product;
import com.kodewala.ecommerce.repository.ProductRepository;

import java.util.List;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Add a new product
    public void addProduct(String name, String category, double price, int quantity, String brand) {
        if (quantity < 0) throw new InvalidQuantityException("InvalidQuantityException: Quantity cannot be negative.");
        if (price < 0)    throw new IllegalArgumentException("Price cannot be negative.");

        int id = productRepository.generateId();
        Product product = new Product(id, name, category, price, quantity, brand);
        productRepository.addProduct(product);
        System.out.println("\n  ✔ Product added successfully! [ID: " + id + "]");
    }

    // View all products
    public void viewAllProducts() {
        List<Product> list = productRepository.getAllProducts();
        if (list.isEmpty()) {
            System.out.println("  No products available.");
            return;
        }
        System.out.println("\n  ===== ALL PRODUCTS (" + list.size() + ") =====");
        list.forEach(p -> System.out.println(p));
    }

    // Search by ID
    public Product searchById(int id) {
        Product p = productRepository.findById(id);
        System.out.println(p);
        return p;
    }

    // Search by name
    public void searchByName(String name) {
        List<Product> results = productRepository.findByName(name);
        printResults(results, "name '" + name + "'");
    }

    // Search by category
    public void searchByCategory(String category) {
        List<Product> results = productRepository.findByCategory(category);
        printResults(results, "category '" + category + "'");
    }

    // Search by brand
    public void searchByBrand(String brand) {
        List<Product> results = productRepository.findByBrand(brand);
        printResults(results, "brand '" + brand + "'");
    }

    // Search by price range
    public void searchByPriceRange(double min, double max) {
        if (min > max) throw new IllegalArgumentException("Min price cannot be greater than max price.");
        List<Product> results = productRepository.findByPriceRange(min, max);
        printResults(results, "price range $" + min + " - $" + max);
    }

    // Update price
    public void updatePrice(int id, double newPrice) {
        if (newPrice < 0) throw new IllegalArgumentException("Price cannot be negative.");
        Product product = productRepository.findById(id);
        product.setPrice(newPrice);
        System.out.println("  ✔ Price updated to $" + newPrice + " for product [ID: " + id + "]");
    }

    // Update quantity
    public void updateQuantity(int id, int newQuantity) {
        if (newQuantity < 0) throw new InvalidQuantityException("InvalidQuantityException: Quantity cannot be negative.");
        Product product = productRepository.findById(id);
        product.setQuantity(newQuantity);
        System.out.println("  ✔ Quantity updated to " + newQuantity + " for product [ID: " + id + "]");
    }

    // Delete product
    public void deleteProduct(int id) {
        productRepository.deleteProduct(id);
        System.out.println("  ✔ Product [ID: " + id + "] deleted successfully.");
    }

    // Get product (used by other services)
    public Product getProduct(int id) {
        return productRepository.findById(id);
    }

    // Helper
    private void printResults(List<Product> results, String searchFor) {
        if (results.isEmpty()) {
            System.out.println("  No products found for " + searchFor + ".");
            return;
        }
        System.out.println("\n  ===== SEARCH RESULTS (" + results.size() + ") for " + searchFor + " =====");
        results.forEach(System.out::println);
    }
}

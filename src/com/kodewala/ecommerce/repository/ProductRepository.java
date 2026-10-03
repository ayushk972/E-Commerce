package com.kodewala.ecommerce.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kodewala.ecommerce.exception.ProductNotFoundException;
import com.kodewala.ecommerce.model.Product;

public class ProductRepository {

    private final List<Product> products = new ArrayList<>();
    private int idCounter = 1;

    public ProductRepository() {
        // Pre-loaded sample data
        products.add(new Product(idCounter++, "iPhone 16",      "Electronics", 999.99,  10, "Apple"));
        products.add(new Product(idCounter++, "Samsung Galaxy S24", "Electronics", 849.99, 8, "Samsung"));
        products.add(new Product(idCounter++, "MacBook Air M3",  "Laptops",      1299.99, 5, "Apple"));
        products.add(new Product(idCounter++, "Dell XPS 15",     "Laptops",      1199.99, 6, "Dell"));
        products.add(new Product(idCounter++, "Nike Air Max",    "Footwear",      129.99, 20, "Nike"));
        products.add(new Product(idCounter++, "Adidas Ultraboost","Footwear",     159.99, 15, "Adidas"));
        products.add(new Product(idCounter++, "Levi's Jeans",   "Clothing",       69.99, 30, "Levi's"));
        products.add(new Product(idCounter++, "Sony WH-1000XM5","Electronics",   349.99, 12, "Sony"));
    }

    // Generate next ID
    public int generateId() {
        return idCounter++;
    }

    // Add product
    public void addProduct(Product product) {
        products.add(product);
    }

    // Get all products
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    // Find by ID
    public Product findById(int id) {
        return products.stream()
                .filter(p -> p.getProductId() == id)
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(
                        "ProductNotFoundException: Product with ID " + id + " does not exist."));
    }

    // Find by name (case-insensitive partial match)
    public List<Product> findByName(String name) {
        String lower = name.toLowerCase();
        return products.stream()
                .filter(p -> p.getProductName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    // Find by category
    public List<Product> findByCategory(String category) {
        String lower = category.toLowerCase();
        return products.stream()
                .filter(p -> p.getCategory().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    // Find by brand
    public List<Product> findByBrand(String brand) {
        String lower = brand.toLowerCase();
        return products.stream()
                .filter(p -> p.getBrand().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    // Find by price range
    public List<Product> findByPriceRange(double min, double max) {
        return products.stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .sorted(java.util.Comparator.comparingDouble(Product::getPrice))
                .collect(Collectors.toList());
    }

    // Delete product
    public void deleteProduct(int id) {
        Product product = findById(id); // throws if not found
        products.remove(product);
    }

    // Check product exists
    public boolean existsById(int id) {
        return products.stream().anyMatch(p -> p.getProductId() == id);
    }
}

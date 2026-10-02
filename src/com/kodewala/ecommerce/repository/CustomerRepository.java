package com.kodewala.ecommerce.repository;

import com.kodewala.ecommerce.exception.CustomerNotFoundException;
import com.kodewala.ecommerce.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomerRepository {

    // Map<customerId, Customer>
    private final Map<Integer, Customer> customers = new HashMap<>();
    private int idCounter = 101;

    // Generate next ID
    public int generateId() {
        return idCounter++;
    }

    // Register customer
    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }

    // Find by ID
    public Customer findById(int id) {
        Customer customer = customers.get(id);
        if (customer == null) {
            throw new CustomerNotFoundException(
                    "CustomerNotFoundException: Customer with ID " + id + " does not exist.");
        }
        return customer;
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    // Find by email (for login)
    public Optional<Customer> findByEmail(String email) {
        return customers.values().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    // Check email already registered
    public boolean emailExists(String email) {
        return customers.values().stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email));
    }
}

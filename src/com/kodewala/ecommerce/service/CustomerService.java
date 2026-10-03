package com.kodewala.ecommerce.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.kodewala.ecommerce.exception.CustomerNotFoundException;
import com.kodewala.ecommerce.model.Customer;
import com.kodewala.ecommerce.repository.CustomerRepository;

public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Register a new customer
    public Customer register(String name, String email, String mobile, String address) {
        if (customerRepository.emailExists(email)) {
            throw new IllegalArgumentException("Email '" + email + "' is already registered.");
        }
        int id = customerRepository.generateId();
        Customer customer = new Customer(id, name, email, mobile, address);
        customerRepository.addCustomer(customer);
        System.out.println("\n  Registration successful! Your Customer ID: " + id);
        return customer;
    }

    // Login by email
    public Customer login(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException(
                    "CustomerNotFoundException: No account found with email '" + email + "'.");
        }
        System.out.println("\n  Welcome back, " + customer.get().getName() + "!");
        return customer.get();
    }

    // View customer details
    public void viewCustomer(int id) {
        Customer c = customerRepository.findById(id);
        System.out.println(c);
    }

    // Search customer by ID
    public Customer searchById(int id) {
        return customerRepository.findById(id);
    }

    // Update customer address
    public void updateAddress(int id, String newAddress) {
        Customer customer = customerRepository.findById(id);
        customer.setAddress(newAddress);
        System.out.println("  Address updated successfully for Customer [ID: " + id + "]");
    }

    // View all customers
    public void viewAllCustomers() {
        List<Customer> list = customerRepository.getAllCustomers();
        if (list.isEmpty()) {
            System.out.println("  No customers registered yet.");
            return;
        }
        System.out.println("\n  ===== ALL CUSTOMERS (" + list.size() + ") =====");
        list.stream()
            .sorted(Comparator.comparingInt(Customer::getCustomerId))
            .forEach(System.out::println);
    }
}

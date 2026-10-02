package com.kodewala.ecommerce.main;

import com.kodewala.ecommerce.model.Customer;
import com.kodewala.ecommerce.repository.CustomerRepository;
import com.kodewala.ecommerce.repository.OrderRepository;
import com.kodewala.ecommerce.repository.ProductRepository;
import com.kodewala.ecommerce.service.*;

import java.util.Scanner;

public class EcommerceApplication {

    // ── Repositories ──────────────────────────────────────────────
    private static final ProductRepository  productRepo  = new ProductRepository();
    private static final CustomerRepository customerRepo = new CustomerRepository();
    private static final OrderRepository    orderRepo    = new OrderRepository();

    // ── Services ──────────────────────────────────────────────────
    private static final ProductService  productService  = new ProductService(productRepo);
    private static final CustomerService customerService = new CustomerService(customerRepo);
    private static final CartService     cartService     = new CartService(productService);
    private static final OrderService    orderService    = new OrderService(orderRepo, cartService, productService);

    private static final Scanner sc = new Scanner(System.in);

    // ══════════════════════════════════════════════════════════════
    //  ENTRY POINT
    // ══════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> adminMenu();
                case 2 -> customerMenu();
                case 3 -> { System.out.println("\n  Goodbye! 👋"); running = false; }
                default -> System.out.println("  ⚠ Invalid choice. Try again.");
            }
        }
        sc.close();
    }

    // ══════════════════════════════════════════════════════════════
    //  MAIN MENU
    // ══════════════════════════════════════════════════════════════
    private static void printMainMenu() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║       MAIN MENU              ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Admin                    ║");
        System.out.println("║  2. Customer                 ║");
        System.out.println("║  3. Exit                     ║");
        System.out.println("╚══════════════════════════════╝");
    }

    // ══════════════════════════════════════════════════════════════
    //  ADMIN MENU
    // ══════════════════════════════════════════════════════════════
    private static void adminMenu() {
        boolean inAdmin = true;
        while (inAdmin) {
            printAdminMenu();
            int choice = readInt("Admin choice: ");
            try {
                switch (choice) {
                    case 1 -> adminAddProduct();
                    case 2 -> productService.viewAllProducts();
                    case 3 -> adminSearchProduct();
                    case 4 -> adminUpdateProduct();
                    case 5 -> adminDeleteProduct();
                    case 6 -> customerService.viewAllCustomers();
                    case 7 -> orderService.viewAllOrders();
                    case 8 -> { System.out.println("  ← Back to Main Menu"); inAdmin = false; }
                    default -> System.out.println("  ⚠ Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("\n  ✘ ERROR: " + e.getMessage());
            }
        }
    }

    private static void printAdminMenu() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║       ADMIN PANEL            ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Add Product              ║");
        System.out.println("║  2. View All Products        ║");
        System.out.println("║  3. Search Product           ║");
        System.out.println("║  4. Update Product           ║");
        System.out.println("║  5. Delete Product           ║");
        System.out.println("║  6. View Customers           ║");
        System.out.println("║  7. View All Orders          ║");
        System.out.println("║  8. Exit Admin               ║");
        System.out.println("╚══════════════════════════════╝");
    }

    private static void adminAddProduct() {
        System.out.println("\n  --- Add New Product ---");
        System.out.print("  Product Name : "); String name     = sc.nextLine().trim();
        System.out.print("  Category     : "); String category = sc.nextLine().trim();
        System.out.print("  Brand        : "); String brand    = sc.nextLine().trim();
        double price    = readDouble("  Price ($)    : ");
        int    quantity = readInt("  Quantity     : ");
        productService.addProduct(name, category, price, quantity, brand);
    }

    private static void adminSearchProduct() {
        System.out.println("\n  Search by: 1.ID  2.Name  3.Category  4.Brand  5.Price Range");
        int opt = readInt("  Option: ");
        switch (opt) {
            case 1 -> { int id = readInt("  Product ID: "); productService.searchById(id); }
            case 2 -> { System.out.print("  Name: "); productService.searchByName(sc.nextLine().trim()); }
            case 3 -> { System.out.print("  Category: "); productService.searchByCategory(sc.nextLine().trim()); }
            case 4 -> { System.out.print("  Brand: "); productService.searchByBrand(sc.nextLine().trim()); }
            case 5 -> {
                double min = readDouble("  Min Price: ");
                double max = readDouble("  Max Price: ");
                productService.searchByPriceRange(min, max);
            }
            default -> System.out.println("  ⚠ Invalid option.");
        }
    }

    private static void adminUpdateProduct() {
        int id = readInt("\n  Product ID to update: ");
        System.out.println("  Update: 1.Price  2.Quantity");
        int opt = readInt("  Option: ");
        if (opt == 1) {
            double price = readDouble("  New Price ($): ");
            productService.updatePrice(id, price);
        } else if (opt == 2) {
            int qty = readInt("  New Quantity: ");
            productService.updateQuantity(id, qty);
        } else {
            System.out.println("  ⚠ Invalid option.");
        }
    }

    private static void adminDeleteProduct() {
        int id = readInt("\n  Product ID to delete: ");
        System.out.print("  Confirm delete? (yes/no): ");
        String confirm = sc.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes")) {
            productService.deleteProduct(id);
        } else {
            System.out.println("  Delete cancelled.");
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  CUSTOMER MENU
    // ══════════════════════════════════════════════════════════════
    private static void customerMenu() {
        Customer loggedIn = null;

        // Register or Login first
        System.out.println("\n  1. Register   2. Login");
        int opt = readInt("  Option: ");
        try {
            if (opt == 1) {
                loggedIn = registerCustomer();
            } else if (opt == 2) {
                System.out.print("  Enter Email: ");
                String email = sc.nextLine().trim();
                loggedIn = customerService.login(email);
            } else {
                System.out.println("  ⚠ Invalid option.");
                return;
            }
        } catch (Exception e) {
            System.out.println("\n  ✘ ERROR: " + e.getMessage());
            return;
        }

        // Logged-in session
        boolean inCustomer = true;
        while (inCustomer) {
            printCustomerMenu(loggedIn.getName());
            int choice = readInt("Your choice: ");
            try {
                switch (choice) {
                    case 1  -> productService.viewAllProducts();
                    case 2  -> customerSearchProduct();
                    case 3  -> customerAddToCart(loggedIn.getCustomerId());
                    case 4  -> cartService.viewCart(loggedIn.getCustomerId());
                    case 5  -> customerRemoveFromCart(loggedIn.getCustomerId());
                    case 6  -> customerChangeQuantity(loggedIn.getCustomerId());
                    case 7  -> orderService.placeOrder(loggedIn.getCustomerId());
                    case 8  -> orderService.viewCustomerOrders(loggedIn.getCustomerId());
                    case 9  -> customerCancelOrder(loggedIn.getCustomerId());
                    case 10 -> customerService.viewCustomer(loggedIn.getCustomerId());
                    case 11 -> {
                        String newAddr = readLine("  New Address: ");
                        customerService.updateAddress(loggedIn.getCustomerId(), newAddr);
                    }
                    case 12 -> {
                        System.out.println("  ✔ Logged out. Goodbye, " + loggedIn.getName() + "!");
                        inCustomer = false;
                    }
                    default -> System.out.println("  ⚠ Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("\n  ✘ ERROR: " + e.getMessage());
            }
        }
    }

    private static void printCustomerMenu(String name) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.printf( "║  Hello, %-29s║%n", name + "!");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║   1.  View Products                  ║");
        System.out.println("║   2.  Search Product                 ║");
        System.out.println("║   3.  Add Product to Cart            ║");
        System.out.println("║   4.  View Cart                      ║");
        System.out.println("║   5.  Remove Product from Cart       ║");
        System.out.println("║   6.  Change Cart Item Quantity      ║");
        System.out.println("║   7.  Place Order                    ║");
        System.out.println("║   8.  View My Orders                 ║");
        System.out.println("║   9.  Cancel Order                   ║");
        System.out.println("║  10.  View My Profile                ║");
        System.out.println("║  11.  Update My Address              ║");
        System.out.println("║  12.  Logout                         ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    private static Customer registerCustomer() {
        System.out.println("\n  --- Customer Registration ---");
        System.out.print("  Name     : "); String name    = sc.nextLine().trim();
        System.out.print("  Email    : "); String email   = sc.nextLine().trim();
        System.out.print("  Mobile   : "); String mobile  = sc.nextLine().trim();
        System.out.print("  Address  : "); String address = sc.nextLine().trim();
        return customerService.register(name, email, mobile, address);
    }

    private static void customerSearchProduct() {
        System.out.println("\n  Search by: 1.ID  2.Name  3.Category  4.Brand  5.Price Range");
        int opt = readInt("  Option: ");
        switch (opt) {
            case 1 -> { int id = readInt("  Product ID: "); productService.searchById(id); }
            case 2 -> { System.out.print("  Name: "); productService.searchByName(sc.nextLine().trim()); }
            case 3 -> { System.out.print("  Category: "); productService.searchByCategory(sc.nextLine().trim()); }
            case 4 -> { System.out.print("  Brand: "); productService.searchByBrand(sc.nextLine().trim()); }
            case 5 -> {
                double min = readDouble("  Min Price ($): ");
                double max = readDouble("  Max Price ($): ");
                productService.searchByPriceRange(min, max);
            }
            default -> System.out.println("  ⚠ Invalid option.");
        }
    }

    private static void customerAddToCart(int customerId) {
        int productId = readInt("\n  Product ID to add: ");
        int quantity  = readInt("  Quantity: ");
        cartService.addToCart(customerId, productId, quantity);
    }

    private static void customerRemoveFromCart(int customerId) {
        int productId = readInt("\n  Product ID to remove from cart: ");
        cartService.removeFromCart(customerId, productId);
    }

    private static void customerChangeQuantity(int customerId) {
        int productId = readInt("\n  Product ID: ");
        System.out.println("  1. Increase  2. Decrease");
        int opt    = readInt("  Option: ");
        int amount = readInt("  Amount: ");
        if (opt == 1) {
            cartService.increaseQuantity(customerId, productId, amount);
        } else if (opt == 2) {
            cartService.decreaseQuantity(customerId, productId, amount);
        } else {
            System.out.println("  ⚠ Invalid option.");
        }
    }

    private static void customerCancelOrder(int customerId) {
        System.out.print("\n  Enter Order ID to cancel: ");
        String orderId = sc.nextLine().trim();
        orderService.cancelOrder(orderId, customerId);
    }

    // ══════════════════════════════════════════════════════════════
    //  HELPERS
    // ══════════════════════════════════════════════════════════════
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Please enter a valid number.");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                                                  ║");
        System.out.println("║    🛒  KodeWala E-Commerce Store  🛒             ║");
        System.out.println("║         Console-Based Mini Project               ║");
        System.out.println("║                                                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }
}

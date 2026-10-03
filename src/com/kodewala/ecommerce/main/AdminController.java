//package com.kodewala.ecommerce.main;
//
//import com.kodewala.ecommerce.repository.CustomerRepository;
//import com.kodewala.ecommerce.repository.OrderRepository;
//import com.kodewala.ecommerce.repository.ProductRepository;
//import com.kodewala.ecommerce.service.CartService;
//import com.kodewala.ecommerce.service.CustomerService;
//import com.kodewala.ecommerce.service.OrderService;
//import com.kodewala.ecommerce.service.ProductService;
//
//public class AdminController {
//	
//	// ── Repositories ──────────────────────────────────────────────
//    static final ProductRepository  productRepo  = new ProductRepository();
//    static final CustomerRepository customerRepo = new CustomerRepository();
//    static final OrderRepository    orderRepo    = new OrderRepository();
//
//    // ── Services ──────────────────────────────────────────────────
//    static final ProductService  productService  = new ProductService(productRepo);
//    static final CustomerService customerService = new CustomerService(customerRepo);
//    static final CartService     cartService     = new CartService(productService);
//    static final OrderService    orderService    = new OrderService(orderRepo, cartService, productService);
//
//	
//	
//	
//	
//
//    // ══════════════════════════════════════════════════════════════
//    //  MAIN MENU
//    // ══════════════════════════════════════════════════════════════
//     static void printMainMenu() {
//        System.out.println("\n╔══════════════════════════════╗");
//        System.out.println("║       MAIN MENU              ║");
//        System.out.println("╠══════════════════════════════╣");
//        System.out.println("║  1. Admin                    ║");
//        System.out.println("║  2. Customer                 ║");
//        System.out.println("║  3. Exit                     ║");
//        System.out.println("╚══════════════════════════════╝");
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    //  ADMIN MENU =======‖‖‖‗‗
//    // ══════════════════════════════════════════════════════════════
//    private static void adminMenu() {
//        boolean inAdmin = true;
//        while (inAdmin) {
//            printAdminMenu();
//            int choice = readInt("Admin choice: ");
//            try {
//                switch (choice) {
//                    case 1 -> adminAddProduct();
//                    case 2 -> productService.viewAllProducts();
//                    case 3 -> adminSearchProduct();
//                    case 4 -> adminUpdateProduct();
//                    case 5 -> adminDeleteProduct();
//                    case 6 -> customerService.viewAllCustomers();
//                    case 7 -> orderService.viewAllOrders();
//                    case 8 -> { System.out.println("  ← Back to Main Menu"); inAdmin = false; }
//                    default -> System.out.println("  Invalid choice.");
//                }
//            } catch (Exception e) {
//                System.out.println("\n  ERROR: " + e.getMessage());
//            }
//        }
//    }
//
//    private static void printAdminMenu() {
//        System.out.println("\n╔══════════════════════════════╗");
//        System.out.println("║       ADMIN PANEL            ║");
//        System.out.println("╠══════════════════════════════╣");
//        System.out.println("║  1. Add Product              ║");
//        System.out.println("║  2. View All Products        ║");
//        System.out.println("║  3. Search Product           ║");
//        System.out.println("║  4. Update Product           ║");
//        System.out.println("║  5. Delete Product           ║");
//        System.out.println("║  6. View Customers           ║");
//        System.out.println("║  7. View All Orders          ║");
//        System.out.println("║  8. Exit Admin               ║");
//        System.out.println("╚══════════════════════════════╝");
//    }
//
//    private static void adminAddProduct() {
//        System.out.println("\n  --- Add New Product ---");
//        System.out.print("  Product Name : "); String name     = sc.nextLine().trim();
//        System.out.print("  Category     : "); String category = sc.nextLine().trim();
//        System.out.print("  Brand        : "); String brand    = sc.nextLine().trim();
//        double price    = readDouble("  Price (₹)    : ");
//        int    quantity = readInt("  Quantity     : ");
//        productService.addProduct(name, category, price, quantity, brand);
//    }
//
//    private static void adminSearchProduct() {
//        System.out.println("\n  Search by: 1.ID  2.Name  3.Category  4.Brand  5.Price Range");
//        int opt = readInt("  Option: ");
//        switch (opt) {
//            case 1 -> { int id = readInt("  Product ID: "); productService.searchById(id); }
//            case 2 -> { System.out.print("  Name: "); productService.searchByName(sc.nextLine().trim()); }
//            case 3 -> { System.out.print("  Category: "); productService.searchByCategory(sc.nextLine().trim()); }
//            case 4 -> { System.out.print("  Brand: "); productService.searchByBrand(sc.nextLine().trim()); }
//            case 5 -> {
//                double min = readDouble("  Min Price: ");
//                double max = readDouble("  Max Price: ");
//                productService.searchByPriceRange(min, max);
//            }
//            default -> System.out.println(" Invalid option.");
//        }
//    }
//
//    private static void adminUpdateProduct() {
//        int id = readInt("\n  Product ID to update: ");
//        System.out.println("  Update: 1.Price  2.Quantity");
//        int opt = readInt("  Option: ");
//        if (opt == 1) {
//            double price = readDouble("  New Price (₹): ");
//            productService.updatePrice(id, price);
//        } else if (opt == 2) {
//            int qty = readInt("  New Quantity: ");
//            productService.updateQuantity(id, qty);
//        } else {
//            System.out.println("  Invalid option.");
//        }
//    }
//
//    private static void adminDeleteProduct() {
//        int id = readInt("\n  Product ID to delete: ");
//        System.out.print("  Confirm delete? (yes/no): ");
//        String confirm = sc.nextLine().trim();
//        if (confirm.equalsIgnoreCase("yes")) {
//            productService.deleteProduct(id);
//        } else {
//            System.out.println("  Delete cancelled.");
//        }
//    }
//
//}

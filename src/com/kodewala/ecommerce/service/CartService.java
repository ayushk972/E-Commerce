package com.kodewala.ecommerce.service;

import com.kodewala.ecommerce.exception.InsufficientStockException;
import com.kodewala.ecommerce.exception.InvalidQuantityException;
import com.kodewala.ecommerce.exception.ProductNotFoundException;
import com.kodewala.ecommerce.model.CartItem;
import com.kodewala.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartService {

	// Map<customerId, List<CartItem>>
	private final Map<Integer, List<CartItem>> carts = new HashMap<>();
	private final ProductService productService;

	public CartService(ProductService productService) {
		this.productService = productService;
	}

	// Ensure cart exists for customer
	private List<CartItem> getOrCreateCart(int customerId) {
		carts.putIfAbsent(customerId, new ArrayList<>());
		return carts.get(customerId);
	}

	// Add product to cart
	public void addToCart(int customerId, int productId, int quantity) {
		if (quantity <= 0)
			throw new InvalidQuantityException("InvalidQuantityException: Quantity must be greater than 0.");

		Product product = productService.getProduct(productId);

		if (product.getQuantity() < quantity) {
			throw new InsufficientStockException("InsufficientStockException: Only " + product.getQuantity()
					+ " unit(s) of '" + product.getProductName() + "' available in stock.");
		}

		List<CartItem> cart = getOrCreateCart(customerId);

		// Check if product already in cart
		for (CartItem item : cart) {
			if (item.getProductId() == productId) {
				int newQty = item.getQuantity() + quantity;
				if (product.getQuantity() < newQty) {
					throw new InsufficientStockException("InsufficientStockException: Only " + product.getQuantity()
							+ " unit(s) available. Cart already has " + item.getQuantity() + " unit(s).");
				}
				item.setQuantity(newQty);
				System.out.println("  Updated cart: '" + product.getProductName() + "' quantity is now " + newQty);
				return;
			}
		}

		cart.add(new CartItem(productId, product.getProductName(), product.getPrice(), quantity));
		System.out.println("  Added to cart: '" + product.getProductName() + "' x" + quantity);
	}

	// Remove product from cart
	public void removeFromCart(int customerId, int productId) {
		List<CartItem> cart = getOrCreateCart(customerId);
		/* if(cart.isEmpty()) return; */
		boolean removed = cart.removeIf(item -> item.getProductId() == productId);
		if (!removed) {
			throw new ProductNotFoundException("Product [ID: " + productId + "] not found in your cart.");
		}
		System.out.println("  Product [ID: " + productId + "] removed from cart.");
	}

	// Increase quantity
	public void increaseQuantity(int customerId, int productId, int qty) {
		if (qty <= 0)
			throw new InvalidQuantityException("InvalidQuantityException: Qty must be positive.");
		List<CartItem> cart = getOrCreateCart(customerId);
		CartItem item = findCartItem(cart, productId);
		Product product = productService.getProduct(productId);
		int newQty = item.getQuantity() + qty;
		if (product.getQuantity() < newQty) {
			throw new InsufficientStockException(
					"InsufficientStockException: Only " + product.getQuantity() + " unit(s) available.");
		}
		item.setQuantity(newQty);
		System.out.println("  Quantity increased to " + newQty + " for '" + item.getProductName() + "'");
	}

	// Decrease quantity
	public void decreaseQuantity(int customerId, int productId, int qty) {
		if (qty <= 0)
			throw new InvalidQuantityException("InvalidQuantityException: Qty must be positive.");
		List<CartItem> cart = getOrCreateCart(customerId);
		CartItem item = findCartItem(cart, productId);
		int newQty = item.getQuantity() - qty;
		if (newQty <= 0) {
			cart.remove(item);
			System.out.println("  Item removed from cart (quantity reached 0).");
		} else {
			item.setQuantity(newQty);
			System.out.println("  Quantity decreased to " + newQty + " for '" + item.getProductName() + "'");
		}
	}

	// View cart
	public void viewCart(int customerId) {
		List<CartItem> cart = getOrCreateCart(customerId);
		if (cart.isEmpty()) {
			System.out.println("  Your cart is empty.");
			return;
		}
		System.out.println("\n  ===== YOUR SHOPPING CART =====");
		cart.forEach(System.out::println);
		System.out.printf("  -------------------------------%n");
		System.out.printf("  TOTAL: ₹%.2f%n", calculateTotal(customerId));
		System.out.println("  ================================");
	}

	// Calculate total
	public double calculateTotal(int customerId) {
		return getOrCreateCart(customerId).stream().mapToDouble(CartItem::getTotalPrice).sum();
	}

	// Clear cart
	public void clearCart(int customerId) {
		getOrCreateCart(customerId).clear();
		System.out.println("  Cart cleared.");
	}

	// Get cart items (for order placement)
	public List<CartItem> getCartItems(int customerId) {
		return new ArrayList<>(getOrCreateCart(customerId));
	}

	// Internal helper
	private CartItem findCartItem(List<CartItem> cart, int productId) {
		return cart.stream().filter(item -> item.getProductId() == productId).findFirst().orElseThrow(
				() -> new ProductNotFoundException("Product [ID: " + productId + "] not found in your cart."));
	}
}

package com.hexaware.entity;

import java.util.Map;

public class Cart {
    private int cartId;
    private int customerId;
    private int productId;
    private int quantity;
    
	public Cart(int cartId, int customerId, int productId, int quantity) {
		super();
		this.cartId = cartId;
		this.customerId = customerId;
		this.productId = productId;
		this.quantity = quantity;
	}


	public Cart() {
		// TODO Auto-generated constructor stub
	}


	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Map<Product, Integer> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Map<Product, Integer> cartItems) {
		this.cartItems = cartItems;
	}
	private Map<Product, Integer> cartItems;

	
    

    
}

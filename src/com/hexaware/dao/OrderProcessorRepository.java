package com.hexaware.dao;

import java.util.List;
import java.util.Map;

import com.hexaware.entity.Cart;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Product;

public interface OrderProcessorRepository {
	boolean createProduct(Product product);

    boolean createCustomer(Customer customer);

    boolean deleteProduct(int productId);

    boolean deleteCustomer(int customerId);

    boolean addToCart(Customer customer, Product product, int quantity);

    boolean removeFromCart(Customer customer, Product product);

    List<Product> getAllFromCart(Customer customer);

    boolean placeOrder(Customer customer, List<Map<Product, Integer>> productsQuantities, String shippingAddress);

    List<Map<Product, Integer>> getOrdersByCustomer(int customerId);
    
    List<Cart> getCartItemsByCustomer(int customerId);
}
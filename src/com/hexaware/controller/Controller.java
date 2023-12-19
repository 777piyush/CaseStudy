package com.hexaware.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.hexaware.dao.OrderProcessorRepositoryImpl;
import com.hexaware.entity.Cart;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Product;
import com.hexaware.exception.CustomerNotFoundException;
import com.hexaware.exception.ProductNotFoundException;

/**
 * Controller class to manage operations related to customers, products, and orders.
 */

public class Controller  {
	private Cart cartDao;
	
	OrderProcessorRepositoryImpl opri = new OrderProcessorRepositoryImpl();
	Customer customer;
	Product product;
	Scanner sc = new Scanner(System.in);
	List  customerlist = new ArrayList();
	private int customerId;
	private int productId;
	
	/**
     * Registers a new customer by collecting their details.
     */
	
	public  void registerCustomer() {
		customer =new Customer();
		System.out.println("Enter the Customer Id");
		int custid =sc.nextInt();
		customer.setCustomerId(custid);
		
		System.out.println("Enter your name");
		String name=sc.nextLine();
		customer.setName(name);
		
		System.out.println("Enter your email");
		String email=sc.nextLine();
		customer.setEmail(email);
		
		System.out.println("Enter your password");
		String password=sc.nextLine();
		customer.setPassword(password);
			
		opri.createCustomer(customer);
		
	}
	
	/**
     * Creates a new product by gathering its details.
     */
	public void createProduct() {
		
		product = new Product();
		System.out.println("Enter the Product Id");
		int productid =sc.nextInt();
		product.setProductId(productid);
	
		System.out.println("Enter your product name");
		String name=sc.nextLine();
		product.setName(name);
		
		System.out.println("Enter your email");
		double price=sc.nextDouble();
		product.setPrice(price);
		
		System.out.println("Enter your product description");
		String description=sc.nextLine();
		product.setDescription(description);
		
		// connect with dao
		opri.createProduct(product);
	}
	/**
     * Helper method to retrieve product details by ID.
     *
     * @param productId The ID of the product to retrieve.
     * @return Product object containing product details.
     */
	
	public void deleteProduct() {
	    try {
	        Product product = new Product();
	        System.out.println("Enter the Product id you want to delete");
	        int productId = sc.nextInt();
	        sc.nextLine(); // Consume newline

	        product.setProductId(productId);

	        // Call the deleteProduct method from your repository/DAO
	        boolean isProductDeleted = opri.deleteProduct(productId);

	        if (isProductDeleted) {
	            System.out.println("Product deleted successfully.");
	        } else {
	            throw new ProductNotFoundException();
	        }
	    } catch (ProductNotFoundException e) {
	        System.out.println(e.getMessage()); 
	    }
	}
	
	public void addToCart() throws CustomerNotFoundException, ProductNotFoundException {
	    System.out.println("enter customer id");
		int customerId = sc.nextInt();
		System.out.println("enter product id");
		int productId = sc.nextInt();
		System.out.println("enter quantity");
		int quantity = sc.nextInt();

		if (customerId <= 0 || productId <= 0 || quantity <= 0) {
		    System.out.println("Invalid input");
		} else {
		    opri.addToCart(customer, product, quantity);
		    System.out.println("Product added to the cart successfully.");
		}
	}

	public void viewCart() throws CustomerNotFoundException {
	    System.out.println("Enter the customerId");
		int customerId = sc.nextInt();

		if (customerId <= 0) {
		    System.out.println("Invalid customer ID.");
		    return;
		}

		List<Cart> cartItems = opri.getCartItemsByCustomer(customerId);

		// Display cart items
		if (cartItems.isEmpty()) {
		    System.out.println("Cart is empty");
		} else {
		    for (Cart cartItem : cartItems) {
		        System.out.println("Cart ID: " + cartItem.getCartId() +
		                ", Product ID: " + cartItem.getProductId() +
		                ", Customer ID: " + cartItem.getCustomerId() +
		                ", Quantity: " + cartItem.getQuantity());
		    }
		}
	}


	public void placeOrder() {
        // Gather Customer information
        System.out.println("Enter Customer ID: ");
        int customerId = sc.nextInt();
        // Here, collect other customer details if needed

        // Gather product quantities
        List<Map<Product, Integer>> productsQuantities = new ArrayList<>();
        boolean addMoreProducts = true;
        while (addMoreProducts) {
            System.out.println("Enter Product ID: ");
            int productId = sc.nextInt();
            // Here, collect other product details if needed

            System.out.println("Enter Quantity: ");
            int quantity = sc.nextInt();

            // Create a Map of Product and Quantity
            Map<Product, Integer> productQuantityMap = new HashMap<>();
            // Create Product object using productId (retrieve from DAO or create as needed)
            Product product = getProductById(productId); // Implement this method to get product details

            productQuantityMap.put(product, quantity);
            productsQuantities.add(productQuantityMap);

            // Ask if user wants to add more products
            System.out.println("Do you want to add more products? (yes/no): ");
            String choice = sc.next();
            addMoreProducts = choice.equalsIgnoreCase("yes");
        }

        // Gather shipping address
        System.out.println("Enter Shipping Address: ");
        sc.nextLine(); // Consume newline
        String shippingAddress = sc.nextLine();

        // Create Customer object using customerId (retrieve from DAO or create as needed)
        Customer customer = getCustomerById(customerId); // Implement this method to get customer details

        boolean orderPlaced = opri.placeOrder(customer, productsQuantities, shippingAddress);

        if (orderPlaced) {
            System.out.println("Order placed successfully!");
        } else {
            System.out.println("Failed to place the order. Please try again.");
        }
    }

    // Helper method to retrieve product details by ID (you need to implement this)
    private Product getProductById(int productId) {
        // Implement logic to fetch product details from DAO based on productId
        return null; // Replace this with actual product retrieval logic
    }

    // Helper method to retrieve customer details by ID (you need to implement this)
    private Customer getCustomerById(int customerId) {
        // Implement logic to fetch customer details from DAO based on customerId
        return null; // Replace this with actual customer retrieval logic
    }
    /**
     * Displays the orders placed by a particular customer.
     */

    public void displayOrdersByCustomer() {
        System.out.println("Enter Customer ID: ");
        int customerId = sc.nextInt();

        List<Map<Product, Integer>> orders = opri.getOrdersByCustomer(customerId);

        if (orders.isEmpty()) {
            System.out.println("No orders found for customer ID: " + customerId);
        } else {
            for (Map<Product, Integer> order : orders) {
                for (Map.Entry<Product, Integer> entry : order.entrySet()) {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();

                    System.out.println("Product: " + product.getName() +
                            ", Quantity: " + quantity +
                            ", Price: " + product.getPrice() +
                            ", Description: " + product.getDescription());
                    // Display other product details as needed
                }
            }
        }
    }

	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	
	
}



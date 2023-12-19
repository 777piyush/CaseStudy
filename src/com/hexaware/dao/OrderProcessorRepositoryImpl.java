package com.hexaware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hexaware.entity.Cart;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Product;
import com.hexaware.exception.CustomerNotFoundException;
import com.hexaware.exception.ProductNotFoundException;
import com.hexaware.util.DBConnection;


public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {
	
	
	
	Connection connection;
	Statement statement;
	PreparedStatement preparedstatement;
	ResultSet resultset;

	@Override
	public boolean createProduct(Product product) {
		String query = "INSERT INTO product (productId, name, price, description) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        	connection = DBConnection.getDbConnection();
        	
            preparedStatement.setInt(1, product.getProductId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setString(4, product.getDescription());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        
        }

		return false;
	}

	@Override
	public boolean createCustomer(Customer customer) {
		String query = "INSERT INTO customers (customerId, name, email, password) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        	connection = DBConnection.getDbConnection();
    
            preparedStatement.setInt(1, customer.getCustomerId());
            preparedStatement.setString(2, customer.getName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed (log it, throw a custom exception, etc.)
        }
		return false;
	}

	@Override
	public boolean deleteProduct(int productId) {
	    String query = "DELETE FROM product WHERE productId = ?";
	    
	    try (Connection connection = DBConnection.getDbConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        preparedStatement.setInt(1, productId);
	        int rowsAffected = preparedStatement.executeUpdate();

	        if (rowsAffected > 0) {
	            return true;
	        } else {
	            throw new ProductNotFoundException();
	        }

	    } catch (SQLException | ProductNotFoundException e) {
	        e.printStackTrace();
	        
	    }

	    return false;
	}

	@Override
	public boolean deleteCustomer(int customerId)  {
	    String query = "DELETE FROM customers WHERE customerId = ?";
	    
	    try (Connection connection = DBConnection.getDbConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        preparedStatement.setInt(1, customerId);
	        int rowsAffected = preparedStatement.executeUpdate();

	        if (rowsAffected > 0) {
	            return true;
	        } else {
	            throw new CustomerNotFoundException();
	        }

	    } catch (SQLException | CustomerNotFoundException e) {
	        e.printStackTrace();
	        
	    }

	    return false;
	}

	@Override
	public boolean addToCart(Customer customer, Product product, int quantity)  {
	    String checkIfExistsQuery = "SELECT * FROM cart WHERE customerId = ? AND productId = ?";
	    String insertQuery = "INSERT INTO cart (customerId, productId, quantity) VALUES (?, ?, ?)";
	    String updateQuantityQuery = "UPDATE cart SET quantity = ? WHERE customerId = ? AND productId = ?";
	    
	    try {
	        connection = DBConnection.getDbConnection();
	        // Check if the item already exists in the cart
	        PreparedStatement checkIfExistsStatement = connection.prepareStatement(checkIfExistsQuery);
	        checkIfExistsStatement.setInt(1, customer.getCustomerId());
	        checkIfExistsStatement.setInt(2, product.getProductId());

	        ResultSet resultSet = checkIfExistsStatement.executeQuery();

	        if (resultSet.next()) {
	            // If the item exists, update the quantity
	            int existingQuantity = resultSet.getInt("quantity");
	            int newQuantity = existingQuantity + quantity;

	            PreparedStatement updateQuantityStatement = connection.prepareStatement(updateQuantityQuery);
	            updateQuantityStatement.setInt(1, newQuantity);
	            updateQuantityStatement.setInt(2, customer.getCustomerId());
	            updateQuantityStatement.setInt(3, product.getProductId());

	            int rowsAffected = updateQuantityStatement.executeUpdate();

	            return rowsAffected > 0;
	        } else {
	            // If the item doesn't exist, insert a new entry
	            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
	            insertStatement.setInt(1, customer.getCustomerId());
	            insertStatement.setInt(2, product.getProductId());
	            insertStatement.setInt(3, quantity);

	            int rowsAffected = insertStatement.executeUpdate();

	            return rowsAffected > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        
	    }

	    return false;
	}



	@Override
	public boolean removeFromCart(Customer customer, Product product) {
		String deleteQuery = "DELETE FROM cart WHERE customerId = ? AND productId = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
        	connection = DBConnection.getDbConnection();
            preparedStatement.setInt(1, customer.getCustomerId());
            preparedStatement.setInt(2, product.getProductId());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed (log it, throw a custom exception, etc.)
        }

		return false;
	}

	@Override
	public List<Product> getAllFromCart(Customer customer) {
		List<Product> productsInCart = new ArrayList<>();
        String query = "SELECT p.* FROM products p JOIN cart c ON p.productId = c.productId WHERE c.customerId = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customer.getCustomerId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Fetch product details from the result set
                int productId = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("description");

                // Create a Product object and add it to the list
                Product product = new Product(productId, name, price, description);
                productsInCart.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed (log it, throw a custom exception, etc.)
        }
		return productsInCart;
	}

	@Override
	public boolean placeOrder(Customer customer, List<Map<Product, Integer>> productsQuantities, String shippingAddress) {
        // Insert into orders table
        String insertOrderQuery = "INSERT INTO orders (customerId, order_date, totalPrice, shippingAddress) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";
        // Insert into order_items table
        String insertOrderItemQuery = "INSERT INTO orderItems (orderId, productId, quantity) VALUES (?, ?, ?)";

        try {
            // Insert into orders table
            PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
            insertOrderStatement.setInt(1, customer.getCustomerId());

            double totalPrice = calculateTotalPrice(productsQuantities);
            insertOrderStatement.setDouble(2, totalPrice);
            insertOrderStatement.setString(3, shippingAddress);

            int rowsAffected = insertOrderStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated order ID
                ResultSet generatedKeys = insertOrderStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    // Insert into order_items table
                    PreparedStatement insertOrderItemStatement = connection.prepareStatement(insertOrderItemQuery);

                    for (Map<Product, Integer> entry : productsQuantities) {
                        for (Map.Entry<Product, Integer> productEntry : entry.entrySet()) {
                            Product product = productEntry.getKey();
                            int quantity = productEntry.getValue();

                            insertOrderItemStatement.setInt(1, orderId);
                            insertOrderItemStatement.setInt(2, product.getProductId());
                            insertOrderItemStatement.setInt(3, quantity);

                            insertOrderItemStatement.addBatch();
                        }
                    }

                    // Execute batch insert for order_items
                    int[] batchRowsAffected = insertOrderItemStatement.executeBatch();

                    // Check if all rows in the batch were inserted successfully
                    for (int batchRow : batchRowsAffected) {
                        if (batchRow <= 0) {
                            // Rollback the transaction if any row failed to be inserted
                            connection.rollback();
                            return false;
                        }
                    }

                    // Commit the transaction if all rows were inserted successfully
                    connection.commit();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed (log it, throw a custom exception, etc.)
            try {
                // Rollback the transaction in case of an exception
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        }

        return false;
    }

    // Helper method to calculate the total price of the order
    private double calculateTotalPrice(List<Map<Product, Integer>> productsQuantities) {
        double totalPrice = 0.0;

        for (Map<Product, Integer> entry : productsQuantities) {
            for (Map.Entry<Product, Integer> productEntry : entry.entrySet()) {
                Product product = productEntry.getKey();
                int quantity = productEntry.getValue();
                totalPrice += product.getPrice() * quantity;
            }
        }

        return totalPrice;
    }

	@Override
	public List<Map<Product, Integer>> getOrdersByCustomer(int customerId) {
        List<Map<Product, Integer>> orders = new ArrayList<>();
        String query = "SELECT oi.productId, oi.quantity FROM orders o " +
                       "JOIN orderItems oi ON o.orderId = oi.orderId " +
                       "WHERE o.customerId = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("productId");
                int quantity = resultSet.getInt("quantity");

                // Retrieve product details
                Product product = getProductDetails(productId);

                // Create a map entry with Product and quantity, and add it to orders list
                Map<Product, Integer> orderDetails = new HashMap<>();
                orderDetails.put(product, quantity);
                orders.add(orderDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed (log it, throw a custom exception, etc.)
        }

        return orders;
    }

    // Helper method to get product details
    private Product getProductDetails(int productId) {
        // Implement logic to fetch product details from the database based on productId
        // You can create a Product object with the fetched details and return it
        // This logic would involve querying the 'products' table
        return null; // Replace this with actual product retrieval logic
    }
    
    public List<Cart> getCartItemsByCustomer(int customerId) {
        List<Cart> cartItems = new ArrayList<>();
        String query = "SELECT cart_id, product_id, customer_id, quantity FROM Cart WHERE customer_id = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int cartId = resultSet.getInt("cart_id");
                int productId = resultSet.getInt("product_id");
                int customerIdFromDb = resultSet.getInt("customer_id");
                int quantity = resultSet.getInt("quantity");

                Cart cartItem = new Cart();
                cartItem.setCartId(cartId);
                cartItem.setProductId(productId);
                cartItem.setCustomerId(customerIdFromDb);
                cartItem.setQuantity(quantity);

                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed (log it, throw a custom exception, etc.)
        }

        return cartItems;
    }

	
}
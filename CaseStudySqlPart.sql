create Database Ecommerce;
use Ecommerce;

CREATE TABLE customers (
    customerId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255)
);

CREATE TABLE products (
    productId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price DECIMAL(10, 2),
    description TEXT,
    stockQuantity INT
);

CREATE TABLE cart (
    cartId INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT,
    productId INT,
    quantity INT,
    FOREIGN KEY (customerId) REFERENCES customers(customerId),
    FOREIGN KEY (productId) REFERENCES products(productId)
);

CREATE TABLE orders (
    orderId INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT,
    orderDate DATE,
    totalPrice DECIMAL(10, 2),
    shippingAddress VARCHAR(255),
    FOREIGN KEY (customerId) REFERENCES customers(customerId)
);

CREATE TABLE orderItems (
    orderItem_id INT AUTO_INCREMENT PRIMARY KEY,
    orderId INT,
    productId INT,
    quantity INT,
    FOREIGN KEY (orderId) REFERENCES orders(orderId),
    FOREIGN KEY (productId) REFERENCES products(productId)
);

Select * from Customers;

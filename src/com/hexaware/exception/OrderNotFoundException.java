package com.hexaware.exception;

public class OrderNotFoundException extends Exception {
  

	public OrderNotFoundException() {
		System.out.println("Order not found");
    }
}
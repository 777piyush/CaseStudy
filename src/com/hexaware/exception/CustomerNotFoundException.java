package com.hexaware.exception;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException() {
    	System.out.println("Customer not found");
    }
}
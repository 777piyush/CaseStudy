package com.hexaware.entity;

import java.util.Map;

public class Customer {
	private int customerId;
    private String name;
    private String email;
    private String password;
    
	public Customer(int customerId, String name, String email, String password) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public Customer() {
		// TODO Auto-generated constructor stub
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [customerId=" + customerId + ", name=" + name + ", email=" + email + ", password=" + password
				+ "]";
	}

	

	public Map<Product, Integer> getCart() {
		// TODO Auto-generated method stub
		return null;
	}


    
    

}

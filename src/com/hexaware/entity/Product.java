package com.hexaware.entity;

public class Product {
	private int productId;
    private String name;
    private double price;
    private String description;
    
	public Product(int productId, String name, double price, String description) {
		super();
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.description = description;
	}

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}

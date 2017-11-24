package com.bridgephase.store;

public class Product {
	private String upc;
	private String name;
	private double wholesalePrice;
	private double retailPrice;
	private Integer quantity;

	public Product(String upc, String name, double wholesalePrice, double retailPrice, int quantity) {
		this.upc = upc;
		this.name = name;
		this.wholesalePrice = wholesalePrice;
		this.retailPrice = retailPrice;
		this.quantity = quantity;
	}
}

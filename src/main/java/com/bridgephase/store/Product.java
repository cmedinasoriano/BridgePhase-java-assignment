package com.bridgephase.store;

public class Product {
	private String upc;
	private String name;
	private Float wholesalePrice;
	private Float retailPrice;
	private Integer quantity;

	public Product(String upc, String name, float wholesalePrice, float retailPrice, int quantity) {
		this.upc = upc;
		this.name = name;
		this.wholesalePrice = wholesalePrice;
		this.retailPrice = retailPrice;
		this.quantity = quantity;
	}
}

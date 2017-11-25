package com.bridgephase.store;

public class Product {
	private String upc;
	private String name;
	private Float wholesalePrice;
	private Float retailPrice;
	private Integer quantity;

	public Product(String upc, String name, Float wholesalePrice, Float retailPrice, Integer quantity) {
		this.upc = upc;
		this.name = name;
		this.wholesalePrice = wholesalePrice;
		this.retailPrice = retailPrice;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getWholesalePrice() {
		return wholesalePrice;
	}

	public void setWholesalePrice(Float wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}

	public float getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Float retailPrice) {
		this.retailPrice = retailPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getUpc() {
		return upc;
	}
}

package com.example.notificationapi.entity;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

	private String productName;
	private String category;

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	private Set<String> tags;

	private BigDecimal price;

	public Product(String category, String productName, BigDecimal price, Set<String> tags) {
		this.category = category;
		this.productName = productName;
		this.price = price;
		this.tags = tags;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}

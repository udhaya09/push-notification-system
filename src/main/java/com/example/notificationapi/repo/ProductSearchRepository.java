package com.example.notificationapi.repo;

import java.util.List;

import com.example.notificationapi.entity.Product;

public interface ProductSearchRepository {

	List<Product> findByText(String text);
	
}

package com.example.notificationapi.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.notificationapi.entity.Product;

public interface ProductsRepository extends MongoRepository<Product, String>{

}

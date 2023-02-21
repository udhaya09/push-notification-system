package com.example.notificationapi.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.notificationapi.entity.Product;
import com.example.notificationapi.entity.SearchHistory;

public interface SearchHistoryRepository extends MongoRepository<SearchHistory, String>{

}

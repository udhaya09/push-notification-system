package com.example.notificationapi.repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import com.example.notificationapi.entity.Product;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class ProductSearchRepositoryImpl implements ProductSearchRepository {

	@Autowired
	MongoClient client;

	@Autowired
	MongoConverter converter;

	@Override
	public List<Product> findByText(String text) {

		final List<Product> posts = new ArrayList<>();

		MongoDatabase database = client.getDatabase("notification-api-poc");
		MongoCollection<Document> collection = database.getCollection("products");

		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
				new Document("$search",
						new Document("index", "products_index_def").append("text",
								new Document("query", text).append("path",
										Arrays.asList("category", "tags", "productName")))),
				new Document("$sort", new Document("productName", 1L)), new Document("$limit", 4L)));

		result.forEach(doc -> posts.add(converter.read(Product.class, doc)));

		return posts;
	}

}

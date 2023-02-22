package com.example.notificationapi.controller;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notificationapi.dto.ProductDto;
import com.example.notificationapi.dto.ResponseDto;
import com.example.notificationapi.entity.Product;
import com.example.notificationapi.rabbitmq.RabbitMQConfiguration;
import com.example.notificationapi.repo.ProductSearchRepository;
import com.example.notificationapi.repo.ProductsRepository;
import com.example.notificationapi.repo.SearchHistoryRepository;
import com.example.notificationapi.repo.UserDetailsRepository;
import com.example.notificationapi.service.NotificationService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	@Autowired
	ProductsRepository productRepository;

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Autowired
	ProductSearchRepository productSearchRepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	SearchHistoryRepository searchHistoryRepository;
	
	@Autowired
    private RabbitTemplate template;

	@PostMapping
	@ApiOperation(value = "To add new product", notes = "When adding new product, it will save the new product in MongoDB and do check product name, category, tags is present is available in recent search history ")
	public ResponseEntity<ResponseDto> addProducts(@RequestBody ProductDto dto) {
		try {
			Product product = productRepository
					.save(new Product(dto.getCategory(), dto.getProductName(), dto.getPrice(), dto.getTags()));
			
			// notificationService.sendNewProductNotification(product);
			
			template.convertAndSend(RabbitMQConfiguration.EXCHANGE, RabbitMQConfiguration.ROUTING_KEY, product);
			
			ResponseDto response = ResponseDto.getSuccessResponse(product);
			
			return new ResponseEntity<>(response, HttpStatus.CREATED);
			
		} catch (Exception e) {
			System.out.println("logging error: " + e);
			ResponseDto response = ResponseDto.getFailureResponse();
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	@ApiOperation(value = "To get list of all products", notes = "This will simply list out all the products available in the MongoDB")
	public ResponseEntity<ResponseDto> getAllProducts() {
		try {
			List<Product> products = productRepository.findAll();

			ResponseDto response = ResponseDto.getSuccessResponse(products);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			ResponseDto response = ResponseDto.getFailureResponse();
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/search/{query}")
	@ApiOperation(value = "To search products", notes = "This will list out the products whose product name or category or tags matches with search terms. If matches, it send push notfications to those users.")
	public ResponseEntity<ResponseDto> searchProducts(@PathVariable String query) {
		try {
			List<Product> products = productSearchRepository.findByText(query);

			ResponseDto response = ResponseDto.getSuccessResponse(products);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			ResponseDto response = ResponseDto.getFailureResponse();
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

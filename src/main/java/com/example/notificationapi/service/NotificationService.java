package com.example.notificationapi.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.notificationapi.dto.Content;
import com.example.notificationapi.dto.SendNotificationDto;
import com.example.notificationapi.entity.Product;
import com.example.notificationapi.entity.SearchHistory;
import com.example.notificationapi.rabbitmq.RabbitMQConfiguration;
import com.example.notificationapi.repo.ProductSearchRepository;
import com.example.notificationapi.repo.SearchHistoryRepository;
import com.example.notificationapi.repo.UserDetailsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class NotificationService {

	@Autowired
	ProductSearchRepository productSearchRepository;

	@Autowired
	SearchHistoryRepository searchHistoryRepository;

	@Autowired
	UserDetailsRepository userDetailsRepository;

	Function<String, LocalDate> dateConversion = str -> {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(str.substring(0, 10), formatter);
	};	

	ToLongFunction<LocalDate> durationFinder = dt -> ChronoUnit.DAYS.between(dt, LocalDate.now());

	@Value("${onesignal.api.key}")
	private String apiKey;
	
	@Value("${onesignal.app.id}")
	private String appId;

	@RabbitListener(queues = RabbitMQConfiguration.QUEUE)
	public void sendNewProductNotification(Product product) throws JsonProcessingException {

		HashSet<String> crntProd = new HashSet<>();
		crntProd.add(product.getCategory().toLowerCase());
		crntProd.add(product.getProductName().toLowerCase());
		crntProd.addAll(
				product.getTags().stream().map(String::toLowerCase).collect(Collectors.toCollection(HashSet::new)));

		List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();

		Set<String> usersToBeNotified = searchHistoryList.stream()
				.filter(sh -> crntProd.stream()
						.anyMatch(cp -> sh.getSearchTerms().contains(cp)
								&& durationFinder.applyAsLong(dateConversion.apply(sh.getTimeStamp())) < 15))
				.map(sh -> sh.getUserId()).collect(Collectors.toSet());

		List<String> playerIds = usersToBeNotified.stream()
				.map(id -> userDetailsRepository.findById(Integer.parseInt(id)).get().getDeviceDetails().getDeviceId())
				.collect(Collectors.toList());

		SendNotificationDto req = new SendNotificationDto();
		req.setApp_id(appId);

		req.setInclude_external_user_ids(playerIds.toArray(new String[playerIds.size()]));
		req.setName("New Product");

		Content contents = new Content();
		contents.setEn(product.getProductName() + " is now available!");
		contents.setEs("Spanish Message");
		req.setContents(contents);

		// create a WebClient instance
		WebClient webClient = WebClient.create("https://onesignal.com/api/v1");

		// Create an instance of the ObjectMapper class
		ObjectMapper objectMapper = new ObjectMapper();

		// Convert the POJO to a JSON string
		String json = objectMapper.writeValueAsString(req);

		System.out.println(json);

		try {

			// make the POST request and retrieve the response
			Mono<String> resp = webClient.post().uri("/notifications")
					.header(HttpHeaders.AUTHORIZATION, "Basic " + apiKey).contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(json)).retrieve().bodyToMono(String.class);

			// subscribe to the response and handle it
			resp.subscribe(responseBody -> System.out.println("Response: " + responseBody),
					error -> System.err.println("Error: " + error));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

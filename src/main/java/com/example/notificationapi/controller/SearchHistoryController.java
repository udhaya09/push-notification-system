package com.example.notificationapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notificationapi.dto.ResponseDto;
import com.example.notificationapi.entity.SearchHistory;
import com.example.notificationapi.repo.SearchHistoryRepository;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/search-history")
public class SearchHistoryController {
	
	@Autowired
	SearchHistoryRepository searchHistoryRepository;

	@GetMapping
	@ApiOperation(value = "To get list of search history", notes = "This will simply list out all the search history in the MongoDB")
	public ResponseEntity<ResponseDto> getAllSearchHistory() {
		try {
			List<SearchHistory> SearchHistory = searchHistoryRepository.findAll();
			ResponseDto response = ResponseDto.getSuccessResponse(SearchHistory);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			ResponseDto response = ResponseDto.getFailureResponse();
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}

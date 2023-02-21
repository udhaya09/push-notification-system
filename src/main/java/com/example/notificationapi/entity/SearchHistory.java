package com.example.notificationapi.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "search_history")
public class SearchHistory {

	private String userId;
	private String email;
	private String timestamp;
	private String searchTerms;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTimeStamp() {
		return timestamp;
	}

	public void setTimeStamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchHistory [userId=");
		builder.append(userId);
		builder.append(", email=");
		builder.append(email);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", searchTerms=");
		builder.append(searchTerms);
		builder.append("]");
		return builder.toString();
	}

	
	
	

}

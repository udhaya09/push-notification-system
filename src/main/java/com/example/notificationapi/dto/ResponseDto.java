package com.example.notificationapi.dto;

public class ResponseDto {

	private Object result;
	private String message;
	private String description;
	private String statusCode;

	public ResponseDto(Object deviceDetails, String msg, String desc, String status) {
		this.message = msg;
		this.description = desc;
		this.statusCode = status;
		this.result = deviceDetails;

	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public static ResponseDto getSuccessResponse(Object deviceDetails) {
		return new ResponseDto(deviceDetails, "SUCCESS", "Request completed successfully", "200");
	}

	public static ResponseDto getFailureResponse() {
		return new ResponseDto(null, "FAILURE", "Request failed", "500");
	}

}

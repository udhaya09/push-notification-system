package com.example.notificationapi.dto;

import java.util.Arrays;

public class SendNotificationDto {

	private String[] include_external_user_ids;
	
	private String name;
	
	private String app_id;
	
	private Content contents;

	public String[] getInclude_external_user_ids() {
		return include_external_user_ids;
	}

	public void setInclude_external_user_ids(String[] include_external_user_ids) {
		this.include_external_user_ids = include_external_user_ids;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public Content getContents() {
		return contents;
	}

	public void setContents(Content contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SendNotificationDto [include_external_user_ids=");
		builder.append(Arrays.toString(include_external_user_ids));
		builder.append(", name=");
		builder.append(name);
		builder.append(", app_id=");
		builder.append(app_id);
		builder.append(", contents=");
		builder.append(contents);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
}

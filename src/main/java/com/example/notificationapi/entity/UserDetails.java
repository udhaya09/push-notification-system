package com.example.notificationapi.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "user_details")
public class UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String email;
	private String name;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "device_id", referencedColumnName = "id")
	private DeviceDetails deviceDetails;

	public UserDetails() {
		super();
	}

	public UserDetails(String email, String name) {
		this.name = name;
		this.email = email;
	}

	public UserDetails(String email) {
		this.email = email;
		this.name = email.substring(0, email.indexOf("@"));
	}

	public DeviceDetails getDeviceDetails() {
		return deviceDetails;
	}

	public void setDeviceDetails(DeviceDetails deviceDetails) {
		this.deviceDetails = deviceDetails;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

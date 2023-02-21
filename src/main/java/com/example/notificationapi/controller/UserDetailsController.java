package com.example.notificationapi.controller;

import java.util.List;
import java.util.UUID;

import javax.swing.DefaultBoundedRangeModel;

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

import com.example.notificationapi.dto.DeviceRegistrationRespDto;
import com.example.notificationapi.dto.ResponseDto;
import com.example.notificationapi.dto.UserDetailsDto;
import com.example.notificationapi.entity.DeviceDetails;
import com.example.notificationapi.entity.UserDetails;
import com.example.notificationapi.repo.DeviceDetailsRepository;
import com.example.notificationapi.repo.UserDetailsRepository;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/user-details")
public class UserDetailsController {

	@Autowired
	UserDetailsRepository userDetailsRepo;

	@Autowired
	DeviceDetailsRepository deviceDetailsRepo;

	@PostMapping
	@ApiOperation(value = "To add new user", notes = "This will create new user in the MySQL DB")
	public ResponseEntity<ResponseDto> addUserDetails(@RequestBody UserDetailsDto dto) {
		try {
			UserDetails userDetails = userDetailsRepo.save(new UserDetails(dto.getEmail(), dto.getName()));

			ResponseDto response = ResponseDto.getSuccessResponse(userDetails);

			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (Exception e) {
			ResponseDto response = ResponseDto.getFailureResponse();
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	@ApiOperation(value = "To get list of all user details", notes = "This will simply list out all the user details available in the MySQL DB")
	public ResponseEntity<ResponseDto> getAllUsers() {
		try {
			List<UserDetails> userDetails = userDetailsRepo.findAll();

			ResponseDto response = ResponseDto.getSuccessResponse(userDetails);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			ResponseDto response = ResponseDto.getFailureResponse();
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/mobile-register/{email}")
	@ApiOperation(value = "To register device with OneSignal", notes = "This will first check email existing or not, if existing it just do registration with OneSignal and return the external user id. If email not exists, it create new one and then register with OneSignal.")
	public ResponseEntity<ResponseDto> registerWithDevice(@PathVariable("email") String email) {
		try {
			UserDetails userDetails = userDetailsRepo.findByEmail(email);

			DeviceRegistrationRespDto dto = new DeviceRegistrationRespDto();

			if (userDetails == null) {
				UserDetails newUser = userDetailsRepo.save(new UserDetails(email));
				DeviceDetails deviceDetails = new DeviceDetails();
				deviceDetails.setDeviceId(UUID.randomUUID().toString());
				deviceDetails.setActive(true);
				deviceDetailsRepo.save(deviceDetails);
				newUser.setDeviceDetails(deviceDetails);
				userDetailsRepo.save(newUser);

				dto.setAlreadyExist(false);
				dto.setExternalUserId(newUser.getDeviceDetails().getDeviceId());

				ResponseDto response = ResponseDto.getSuccessResponse(dto);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			if (userDetails.getDeviceDetails() == null) {

				DeviceDetails deviceDetails = new DeviceDetails();
				deviceDetails.setDeviceId(UUID.randomUUID().toString());
				deviceDetails.setActive(true);
				deviceDetailsRepo.save(deviceDetails);
				userDetails.setDeviceDetails(deviceDetails);
				userDetailsRepo.save(userDetails);

				dto.setAlreadyExist(false);
				dto.setExternalUserId(userDetails.getDeviceDetails().getDeviceId());

				ResponseDto response = ResponseDto.getSuccessResponse(dto);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			dto.setAlreadyExist(true);
			dto.setExternalUserId(userDetails.getDeviceDetails().getDeviceId());

			ResponseDto response = ResponseDto.getSuccessResponse(dto);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			ResponseDto response = ResponseDto.getFailureResponse();
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

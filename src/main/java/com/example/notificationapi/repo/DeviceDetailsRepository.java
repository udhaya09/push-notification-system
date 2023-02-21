package com.example.notificationapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.notificationapi.entity.DeviceDetails;

@Repository
public interface DeviceDetailsRepository extends JpaRepository<DeviceDetails, Integer>{

}

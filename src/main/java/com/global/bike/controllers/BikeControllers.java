package com.global.bike.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.global.bike.exception.RecordNotFoundException;
import com.global.bike.models.Bike;
import com.global.bike.models.BikeLists;
import com.global.bike.repo.BikeRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/bikes")
@Slf4j
public class BikeControllers {

	@Autowired
	private BikeRepository bikeRepository;
	
	@GetMapping("/ping")
	public String ping() {
		return "OK";
	}
	
	@GetMapping
	public List<Bike> list(){
		List<Bike> bikes = new ArrayList<>();
		return bikes;
	}
	
	@PostMapping("/saveBike")
	public String saveBikeDetails(@RequestBody Bike bike) {
		System.out.println(bike);
		Bike b = bikeRepository.save(bike);
		return b.getId();
	}
	
	@GetMapping("/{id}")
	public Bike get(@PathVariable("id") String id) {
		try {
			return bikeRepository.findById(id);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@GetMapping("/category")
	public List<Bike> getByCategory(){
		try {
			return bikeRepository.getBikesBySerialNumber();
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@GetMapping("/group")
	public List<BikeLists> getBikeListByGroupName(@RequestParam String groupName) throws RecordNotFoundException{
		System.out.println("controller called");
		return bikeRepository.getBikesByGroupName(groupName);
	}
}

package com.global.bike.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.global.bike.exception.RecordNotFoundException;
import com.global.bike.models.Bike;
import com.global.bike.models.BikeLists;
import com.global.bike.repo.BikeRepository;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "http://localhost:4200")
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
	public List<Bike> list() {
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
	public Bike getByBikeId(@PathVariable("id") String id) {
		try {
			Bike b = bikeRepository.findById(id);
			if (b != null)
				return b;
			else
				throw new RecordNotFoundException("no bike found");
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@GetMapping("/category")
	public List<Bike> getByCategory() {
		try {
			return bikeRepository.getBikesBySerialNumber();
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@GetMapping("/group")
	public ResponseEntity<List<BikeLists>> getBikeListByGroupName(@RequestParam String groupName) throws Exception {
		List<BikeLists> bikeLists = bikeRepository.getBikesByGroupName(groupName);
			log.info("Bike List retrieved {}", bikeLists);
//			return bikeLists.get();
			return new ResponseEntity<>(bikeLists, HttpStatus.OK);
	}

}

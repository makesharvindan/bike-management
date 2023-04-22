package com.global.bike.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import com.global.bike.models.Sale;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.Message;
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

	@Autowired
	private KafkaTemplate <String ,Object> kafkaTemplate;



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
//		kafkaTemplate.send("test1",bike);
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

		bikeLists.stream().filter(p->p.getBikeList().
						removeIf(b->Integer.valueOf(b.getId())<2))
				.collect(Collectors.toList());
		log.info("Bike List retrieved {}", bikeLists);
//			return bikeLists.get();
		return new ResponseEntity<>(bikeLists, HttpStatus.OK);
	}

//	@GetMapping("/kafka/{message}")
//	public String kafkaProducer(@PathVariable("message") String message) {
//
//		kafkaTemplate.send("test",message);
//		return "Published "+message;
//	}

	@PostMapping("/kafka/sale")
	public ResponseEntity<String> sales(@RequestBody Sale s) {

//		Map<String,Object> configs = new HashMap<>();
//		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"http://localhost:9092");
//		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//
//		KafkaProducer<String,Sale> kafkaProducer = new KafkaProducer<String,Sale>(configs);
//		ProducerRecord<String,Object> record = new ProducerRecord<>("test",s.getId(),s);

//		kafkaTemplate.send(record);
		kafkaTemplate.send("test-1",s);
		return new ResponseEntity<>("OK",HttpStatus.OK);
//		kafkaProducer.send(record);
	}

}

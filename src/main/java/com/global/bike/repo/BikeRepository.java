package com.global.bike.repo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.global.bike.exception.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.global.bike.exception.RecordNotFoundException;
import com.global.bike.models.Bike;
import com.global.bike.models.BikeLists;
import com.mongodb.BasicDBObject;
import com.mongodb.internal.operation.AggregateOperation;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class BikeRepository {

	@Autowired
	public MongoTemplate mongoTemplate;
	
	public List<Bike> getBikesBySerialNumber(){
		
//		Criteria criteria = Criteria.where("serialNumber").is("12");
		MatchOperation matchOperation = Aggregation.match(Criteria.where("serialNumber").is("12"));
		ProjectionOperation project = Aggregation.project().andInclude("_id");
		
		Aggregation aggregation = Aggregation.newAggregation(matchOperation,project);
		AggregationResults result = mongoTemplate.aggregate(aggregation, "bikes", Bike.class);
		if(result.getMappedResults()!=null)
			return result.getMappedResults();
		return null;
		
	}


	public Bike findById(String id) {
		// TODO Auto-generated method stub
		org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();
		query.addCriteria(Criteria.where("_id").is(id));

		 Bike bike = mongoTemplate.findOne(query,Bike.class);
		 return bike;
	}
	
	public List<Bike> findBySerialNumber(String id) {
		// TODO Auto-generated method stub
		org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();
		query.addCriteria(Criteria.where("serialNumber").is(id));

		 List<Bike> bike = mongoTemplate.find(query,Bike.class);
		 return bike;
	}


	public Bike save(Bike bike) {
		// TODO Auto-generated method stub
		return mongoTemplate.save(bike);
	}
	
	public List<BikeLists> getBikesByGroupName(String groupName) throws Exception {
		
		MatchOperation matchOperation = Aggregation.match(Criteria.where("group").is(groupName));
		GroupOperation groupOperation = Aggregation.group("$group").push(new BasicDBObject("bikeName", "$name")).as("BikeList");
		ProjectionOperation projectionOperation = Aggregation.project().and("_id").as("groupName").andExclude("_id").and("BikeList").as("bikeList");
//		AggregationResults<List<Bike>> results = Aggregation.newAggregation(matchOperation,groupOperation);
		Optional<List<BikeLists>> response;
		AggregationResults<BikeLists> results;
		try {
			 results = mongoTemplate.aggregate(Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation), "bikes", BikeLists.class);
//			response = Optional.ofNullable(results.getMappedResults());
		} catch(RuntimeException e) {
			throw new InternalServerErrorException("Runtime exception while fetching details from Database");
		}
		if(!results.getMappedResults().isEmpty()) {
			return results.getMappedResults();
		}
		else {
			throw new RecordNotFoundException("no records found");
		}
	}
		
}

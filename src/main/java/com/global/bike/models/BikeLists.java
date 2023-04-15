package com.global.bike.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BikeLists {

	private String groupName;
	private List<BikeList> bikeList;
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<BikeList> getBikeList() {
		return bikeList;
	}

	public void setBikeList(List<BikeList> bikeList) {
		this.bikeList = bikeList;
	}


	
}

package com.global.bike.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BikeList {

	private String bikeName;

	public String getBikeName() {
		return bikeName;
	}

	public void setBikeName(String bikeName) {
		this.bikeName = bikeName;
	}
	
}

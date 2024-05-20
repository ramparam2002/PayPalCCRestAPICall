package com.paypalccrestapicall.jsonmodel;

import com.google.gson.annotations.SerializedName;

public class Card {
	String number;
	String expiry;
	
	@SerializedName("security_code")
	String securityCode;
	
	String name;

	public Card(String number, String expiry, String securityCode, String name) {
		super();
		this.number = number;
		this.expiry = expiry;
		this.securityCode = securityCode;
		this.name = name;
	}
}

package com.paypalccrestapicall.jsonmodel;

import com.google.gson.annotations.SerializedName;

public class Amount {
	
	@SerializedName("currency_code")
	String currencyCode;
    String value;
    
    public Amount(String currencyCode, String value) {
        this.currencyCode = currencyCode;
        this.value = value;
    }

}

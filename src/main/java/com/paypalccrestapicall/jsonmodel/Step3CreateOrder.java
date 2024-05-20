package com.paypalccrestapicall.jsonmodel;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Step3CreateOrder {
	String intent;
	
	@SerializedName("purchase_units")
	List<PurchaseUnit> purchaseUnits;
	
	@SerializedName("payment_source")
	PaymentSource paymentSource;

	public Step3CreateOrder(String intent, List<PurchaseUnit> purchaseUnits, PaymentSource paymentSource) {
		super();
		this.intent = intent;
		this.purchaseUnits = purchaseUnits;
		this.paymentSource = paymentSource;
	}
	
	
}

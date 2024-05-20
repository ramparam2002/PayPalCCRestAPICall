package com.paypalccrestapicall;

import java.util.Arrays;

import com.paypalccrestapicall.jsonmodel.Amount;
import com.paypalccrestapicall.jsonmodel.Card;
import com.paypalccrestapicall.jsonmodel.PaymentSource;
import com.paypalccrestapicall.jsonmodel.PurchaseUnit;
import com.paypalccrestapicall.jsonmodel.Step3CreateOrder;

public class MainApplication {

    public static void main(String[] args) {
    	
    	PayPalClient payPalClient = new PayPalClient();
    	
    	//To be changed every run
    	String customerId = "ram05";
    	String uniqueRequestId = "try05";
    	
    	//Step 1 - Get OAuth Token
    	String accessToken =  payPalClient.getOAuthDetails();
    	
    	//Step 2 - Generate Token
    	
    	String clientToken = payPalClient.generateClientToken(accessToken, customerId);
    	
    	
    	//Step 3 - Create Order
    	Amount amount = new Amount("USD", "70");
    	PurchaseUnit purchaseUnit = new PurchaseUnit(amount);
    	Card card = new Card("4032031694349076", "2029-04", "123", "John Doe");
    	PaymentSource paymentSource = new PaymentSource(card);
    	Step3CreateOrder createOrderRequest = new Step3CreateOrder("CAPTURE", Arrays.asList(purchaseUnit),paymentSource);
    	
    	String orderID = payPalClient.createOrder(accessToken, createOrderRequest, uniqueRequestId);
    	
   
    }
}


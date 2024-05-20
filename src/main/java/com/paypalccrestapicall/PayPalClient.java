package com.paypalccrestapicall;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paypalccrestapicall.jsonmodel.Step3CreateOrder;

public class PayPalClient {
	
	Logger logger = LoggerFactory.getLogger(PayPalClient.class);
	
    
    //Common data
    private static final String PAYPAL_BASE_URL = "https://api-m.sandbox.paypal.com";
    private static final String PAYPAL_CLIENT_ID = "";
    private static final String PAYPAL_CLIENT_SECRET = "";

    //Step 1 - Get OAuth
    private static final String PAYPAL_OAUTH2_TOKEN_URL =  PAYPAL_BASE_URL + "/v1/oauth2/token";
    
    //Step 2 - Generate Token
    private static final String PAYPAL_GENERATE_TOKEN_URL = PAYPAL_BASE_URL + "/v1/identity/generate-token";
    
    //Step 3 - Create Order
    private static final String PAYPAL_CREATE_ORDER_URL = PAYPAL_BASE_URL + "/v2/checkout/orders";
    
    
    private void logToConsole(List<String> logs) {
    	logger.info("**************************************************************\n");
    	for(String log : logs) {
    		logger.info(log + "\n");
    	}
    	logger.info("**************************************************************\n");
    }
    
    //Step 1 - Get OAuth Details
    public String getOAuthDetails() {
    	 HttpHeaders headers = new HttpHeaders();
    	 headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    	 headers.setBasicAuth(PAYPAL_CLIENT_ID, PAYPAL_CLIENT_SECRET);
    	 
    	 HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);
    	 
    	 RestTemplate restTemplate = new RestTemplate();
    	 ResponseEntity<String> response = restTemplate.exchange(PAYPAL_OAUTH2_TOKEN_URL, HttpMethod.POST, request, String.class);
    	 
    	 logToConsole(Arrays.asList("Response status code :\n" + response.getStatusCode() + ":" + response.getStatusCodeValue(),
    			 "Response Body :\n" + response.getBody(),
    			 "Response headers :\n" + response.getHeaders()));
    	 
    	 JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
    	 
    	 String accessToken = jsonObject.get("access_token").getAsString();
    	 
    	 logToConsole(Arrays.asList("Access Token : \n" + accessToken + "\n"));
    	 
    	 return accessToken;
    	 
    }
    
    //Step 2 - Generate Client Token
	public String generateClientToken(String accessToken, String customerId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);

		HttpEntity<String> request = new HttpEntity<>("{\"customer_id\": \"" + customerId +"\"}", headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(PAYPAL_GENERATE_TOKEN_URL, HttpMethod.POST, request,
				String.class);

		logToConsole(Arrays.asList(
				"Response status code :\n" + response.getStatusCode() + ":" + response.getStatusCodeValue(),
				"Response Body :\n" + response.getBody(), "Response headers :\n" + response.getHeaders()));

		JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();

		String clientToken = jsonObject.get("client_token").getAsString();

		logToConsole(Arrays.asList("Client Token : \n" + clientToken + "\n"));

		return clientToken;
	}
	
	//Step 3 - Create Order and Complete Payment
	public String createOrder(String accessToken, Step3CreateOrder createOrderRequest, String uniqueRequestId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		headers.set("PayPal-Request-Id", uniqueRequestId);
		
		String requestStr = new Gson().toJson(createOrderRequest);
		
		logToConsole(Arrays.asList("Creating order for :\n" + requestStr));
		

		HttpEntity<String> request = new HttpEntity<>(requestStr, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(PAYPAL_CREATE_ORDER_URL, HttpMethod.POST, request,
				String.class);
		
		logToConsole(Arrays.asList(
				"Response status code :\n" + response.getStatusCode() + ":" + response.getStatusCodeValue(),
				"Response Body :\n" + response.getBody(), "Response headers :\n" + response.getHeaders()));
		
		JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();

		String orderID = jsonObject.get("id").getAsString();

		logToConsole(Arrays.asList("Client Token : \n" + orderID + "\n"));

		return orderID;
	}
    
}

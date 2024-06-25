package com.integrations.orderprocessing.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

@Service
public class HttpClientService {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientService.class);
	
	private static final HttpClient httpClient = HttpClient.newBuilder().build();
	
	public JSONObject onSendGetRequest(String url) {
		logger.info("GET request being called(HttpClientService)...");
		
		return  onSendGetRequestNext(url);
	}
	
	private JSONObject onSendGetRequestNext(String url){
       
		JSONObject jsonResponse = null;
		try {
		HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("GET Response Code: " + response.statusCode());
        System.out.println("GET Response Body: " + response.body());
        
        jsonResponse = JSONObject.fromObject(response.body());
	} catch (URISyntaxException | IOException | InterruptedException e) {
		logger.info("Exception in (HttpClientService->onSendPostRequestWithTokenAuth):: "+e.getMessage());
	}
        
        return jsonResponse;
    }

	public JSONObject onSendPostRequest(String url, JSONObject reqBody) {
		logger.info("POST request being called(HttpClientService)...");
		
		Map<Object, Object> data = jsonObjectToMap(reqBody);
		return onSendPostRequestNext(url, data);
	}
	
	private JSONObject onSendPostRequestNext(String url, Map<Object, Object> data){
     
		JSONObject jsonResponse = null;
		
		try {
		HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .POST(buildJsonData(data))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("POST Response Code: " + response.statusCode());
        System.out.println("POST Response Body: " + response.body());
        
        jsonResponse = JSONObject.fromObject(response.body());
	} catch (URISyntaxException | IOException | InterruptedException e) {
		logger.info("Exception in (HttpClientService->onSendPostRequestWithTokenAuth):: "+e.getMessage());
	}
        
        return jsonResponse;
    }
	
	 public JSONObject onSendPostRequestWithTokenAuth(String url, JSONObject reqBody, String token) {
		 logger.info("POST request with token auth being called(HttpClientService)...");
		 
		 Map<Object, Object> data = jsonObjectToMap(reqBody);
		 return onSendPostRequestWithTokenAuthNext(url, data, token);
	 }

	 private JSONObject onSendPostRequestWithTokenAuthNext(String url, Map<Object, Object> data, String token){
       
		JSONObject jsonResponse = null;
		
		try {
			HttpRequest request = HttpRequest.newBuilder()
			        .uri(new URI(url))
			        .header("Authorization", "Bearer " + token)
			        .header("Content-Type", "application/json")
			        .POST(buildJsonData(data))
			        .build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			System.out.println("POST Response Code: " + response.statusCode());
			System.out.println("POST Response Body: " + response.body());
			
			jsonResponse = JSONObject.fromObject(response.body());
		} catch (URISyntaxException | IOException | InterruptedException e) {
			logger.info("Exception in (HttpClientService->onSendPostRequestWithTokenAuth):: "+e.getMessage());
		}
        return jsonResponse;
    }
	 
	 private  Map<Object, Object> jsonObjectToMap(JSONObject jsonObject) {
	        Map<Object, Object> map = new HashMap<>();
	        jsonObject.keySet().forEach(key -> map.put(key, jsonObject.get(key)));
	        return map;
	    }

    private HttpRequest.BodyPublisher buildJsonData(Map<Object, Object> data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            stringBuilder.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return HttpRequest.BodyPublishers.ofString(stringBuilder.toString());
    }

}

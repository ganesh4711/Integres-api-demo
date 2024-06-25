package com.integrations.orderprocessing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrations.orderprocessing.model.req_body.optioryx.Barcodes;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

@Service
public class WebClientService {
	private static final Logger logger = LoggerFactory.getLogger(WebClientService.class);

	private JSONObject jsonRespErr;
	private JSONArray jsonRespArrayErr;
	private final WebClient webClient;

	public WebClientService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.build();
	}

	public JSONObject onSendGetRequest(String url) throws Exception{
		
		logger.info("GET request being called(WebClientService)...");
		
		 return webClient.get()
	                .uri(url)
	                .retrieve()
	                .bodyToMono(JSONObject.class)
	                .block(); // blocking for simplicity; use subscribe in a non-blocking environment
	}
	
   public JSONObject onSendGetRequestWithTokenAuth(String url, String token) throws Exception{
		
		logger.info("GET request with token auth being called(WebClientService)...");
		
		 return webClient.get()
	                .uri(url)
	                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
	                .retrieve()
	                .bodyToMono(JSONObject.class)
	                .block(); // blocking for simplicity; use subscribe in a non-blocking environment
	}
   
   public JSONObject onSendGetRequestWithBasicAuth(String url, String userName, String password) throws Exception{
		
		logger.info("GET request with basic auth being called(WebClientService)...");
		
		 return webClient.get()
	                .uri(url)
	                .headers(headers -> headers.setBasicAuth(userName, password))
	                .retrieve()
	                .bodyToMono(JSONObject.class)
	                .block(); // blocking for simplicity; use subscribe in a non-blocking environment
	}
   
   public String onSendGetRequestWithBasicAuthByStringResp(String url, String userName, String password) throws Exception{
		
		logger.info("GET request with basic auth being called(WebClientService)...");
		
		 return webClient.get()
	                .uri(url)
	                .headers(headers -> headers.setBasicAuth(userName, password))
	                .retrieve()
	                .bodyToMono(String.class)
	                .block(); // blocking for simplicity; use subscribe in a non-blocking environment
	}

	public JSONObject onSendPostRequest(String url, JSONObject reqBody)  throws Exception{
		
		logger.info("POST request being called(WebClientService)...");
		
		 return webClient.post()
	                .uri(url)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(BodyInserters.fromValue(reqBody))
	                .retrieve()
	                .bodyToMono(JSONObject.class)
	                .block(); // blocking for simplicity; use subscribe in a non-blocking environment
	}
	
public JSONObject onSendPostRequestWithTokenAuth(String url, JSONObject reqBody, String token) throws Exception {
		
		logger.info("POST request with token auth being called(WebClientService)...");
		
		 return webClient.post()
	                .uri(url)
	                .contentType(MediaType.APPLICATION_JSON)
	                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
	                .body(BodyInserters.fromValue(reqBody))
	                .retrieve()
	                .bodyToMono(JSONObject.class)
	                .block(); // blocking for simplicity; use subscribe in a non-blocking environment
	}

public JSONObject onSendDeleteRequest(String url, JSONObject reqBody)  throws Exception{
	
	logger.info("DELETE request being called(WebClientService)...");
	 
	 return webClient.method(HttpMethod.DELETE)
	            .uri(url)
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(BodyInserters.fromValue(reqBody))
	            .retrieve() 
	            .bodyToMono(JSONObject.class)
	            .block();
  }

	public JSONObject onSendGetRequestWithApiKeyAuth(String url, String apiKey) {
		JSONObject jsonResp = new JSONObject();
		jsonRespErr = new JSONObject();

		JSONArray jsonArray = null;
		String remarks = null;

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("x-api-key", apiKey);
		try {
			logger.info("GET request with API key auth being called(WebClientService)...");
			String strResp = WebClient.builder()
					.defaultHeaders(headers -> headers.addAll(httpHeaders))
					.build()
					.get()
					.uri(url)
					.retrieve()
					.onStatus(HttpStatus.UNAUTHORIZED::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.NOT_FOUND::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.UNPROCESSABLE_ENTITY::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.bodyToMono(String.class)
					.block();


			jsonArray =   onParseJsonArrayStr(strResp);
			remarks = "Item(s) fetched successfully";

		} catch (Exception e) {
			remarks = e.getMessage();
			logger.error("Exception while getting data:: {}", remarks);
		}

		jsonResp.put("data", jsonArray);
		String retMsg = onGetErrorMessage(jsonRespErr);
		jsonResp.put("message", (retMsg!=null)? retMsg:remarks);

		return jsonResp;
	}


	public JSONObject onSendPostRequestWithApiKeyAuthandRequestBody(String reqURL, String apiKey, Barcodes barcodes) {
		JSONObject jsonResp = new JSONObject();
		jsonRespErr = new JSONObject();

		JSONArray jsonArray = null;
		String remarks = null;


		try {
			logger.info("POST request with API key auth and requestbody called(WebClientService)...");

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("x-api-key", apiKey);

			String strResp = WebClient.builder()
					.defaultHeaders(headers -> headers.addAll(httpHeaders))
					.build()
					.post()
					.uri(reqURL)
					.bodyValue(barcodes)
					.retrieve()
					.onStatus(HttpStatus.UNAUTHORIZED::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.NOT_FOUND::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.UNPROCESSABLE_ENTITY::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.bodyToMono(String.class)
					.block();

			jsonArray =   onParseJsonArrayStr(strResp);
			remarks = "Item(s) fetched successfully by barcode(s)";
		} catch (Exception e) {
			remarks = e.getMessage();
			logger.error("Exception while getting data:: {}", remarks);
		}

		jsonResp.put("data", jsonArray);
		String retMsg = onGetErrorMessage(jsonRespErr);
		jsonResp.put("message", (retMsg!=null)? retMsg:remarks);

		return jsonResp;

	}
	public JSONObject onSendGetRequestWithApiKey(String reqURL, String apiKey) {
		JSONObject jsonResp = new JSONObject();
		jsonRespErr = new JSONObject();

		String remarks = null;
		String strResp = null;

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("x-api-key", apiKey);

		try {
			logger.info("GET request with API key auth being called(WebClientService)...");

			strResp = WebClient.builder()
					.defaultHeaders(headers -> headers.addAll(httpHeaders))
					.build()
					.get()
					.uri(reqURL)
					.retrieve()
					.onStatus(HttpStatus.UNAUTHORIZED::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.NOT_FOUND::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.UNPROCESSABLE_ENTITY::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.bodyToMono(String.class)
					.block();

			remarks = "Item(s) Count fetched successfully";
		} catch (Exception e) {
			remarks = e.getMessage();
			logger.error("Exception while getting data:: {}", remarks);
		}

		jsonResp.put("data", strResp);
		String retMsg = onGetErrorMessage(jsonRespErr);
		jsonResp.put("message", (retMsg!=null)? retMsg:remarks);

		return jsonResp;
	}
	public JSONObject onSendGetRequestWithApiKeyAndPathVariable(String id, String reqURL, String apiKey) {
		JSONObject jsonResp = new JSONObject();
		jsonRespErr = new JSONObject();

		JSONObject jsonObject = null;
		String remarks = null;

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("x-api-key", apiKey);

		String urlWithPathVariable = reqURL + "/" + id;
		logger.info("optioryx url:: " + urlWithPathVariable);

		try {

			logger.info("GET request with API key auth and path variable called(WebClientService)...");

			String strResp = WebClient.builder()
					.defaultHeaders(headers -> headers.addAll(httpHeaders))
					.build()
					.get()
					.uri(urlWithPathVariable)
					.retrieve()
					.onStatus(HttpStatus.UNAUTHORIZED::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.NOT_FOUND::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.UNPROCESSABLE_ENTITY::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
							response -> response.bodyToMono(String.class).map((data)->onMapHttpErrorResp(data, false))
					)
					.bodyToMono(String.class)
					.block();

			jsonObject =   onParseJsonObjectStr(strResp);
			remarks = "Item fetched successfully by Id";

		} catch (Exception e) {
			remarks = e.getMessage();
			logger.error("Exception while getting data:: {}", remarks);
		}

		jsonResp.put("data", jsonObject);
		String retMsg = onGetErrorMessage(jsonRespErr);
		jsonResp.put("message", (retMsg!=null)? retMsg:remarks);

		return jsonResp;
	}

	public JSONObject onSendPostRequestWithApiKeyAndRequestbody(String reqURL, String  apiKey, Barcodes barcodes) {

		JSONObject jsonResp = new JSONObject();
		jsonRespErr = new JSONObject();

		String remarks = null;
		String strResp = null;


		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("x-api-key", apiKey);

		try {
			logger.info("POST request with API key auth and requestbody called(WebClientService)...");

			strResp = WebClient.builder()
					.defaultHeaders(headers -> headers.addAll(httpHeaders))
					.build()
					.post()
					.uri(reqURL)
					.bodyValue(barcodes)
					.retrieve()
					.onStatus(HttpStatus.UNAUTHORIZED::equals,
							response -> response.bodyToMono(String.class).map((data)->{return onMapHttpErrorResp(data, false);})
					)
					.onStatus(HttpStatus.NOT_FOUND::equals,
							response -> response.bodyToMono(String.class).map((data)->{return onMapHttpErrorResp(data, false);})
					)
					.onStatus(HttpStatus.UNPROCESSABLE_ENTITY::equals,
							response -> response.bodyToMono(String.class).map((data)->{return onMapHttpErrorResp(data, false);})
					)
					.onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
							response -> response.bodyToMono(String.class).map((data)->{return onMapHttpErrorResp(data, false);})
					)
					.bodyToMono(String.class)
					.block();

			remarks = "Item(s) Count fetched successfully by barcode(s)";
		} catch (Exception e) {
			remarks = e.getMessage();
			logger.error("Exception while getting data:: {}", remarks);
		}


		jsonResp.put("data", strResp);
		String retMsg = onGetErrorMessage(jsonRespErr);
		jsonResp.put("message", (retMsg!=null)? retMsg:remarks);

		return jsonResp;

	}

	private JSONArray onParseJsonArrayStr(String strResp) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object source, String name, Object value) {
				return (value == null);
			}
		});

		strResp = ((String) strResp).replace("null", "\"\"");
		return JSONArray.fromObject(strResp, jsonConfig);
	}

	private String onGetErrorMessage(JSONObject jsonRespErr) {
		String retMsg=null;

		try {
			if(jsonRespErr.containsKey("message")) {
				retMsg= jsonRespErr.getString("message");
			}else if(jsonRespErr.containsKey("detail")) {
				JSONArray jsonRespArrayErr = jsonRespErr.getJSONArray("detail");
				JSONObject jsonRespErrTemp = jsonRespArrayErr.getJSONObject(0);
				retMsg=jsonRespErrTemp.getString("msg");
			}
		} catch (Exception e) {
			logger.error("Exception while getting data from Json Response:: {}", e.getMessage());
		}

		return retMsg;
	}

	private Exception onMapHttpErrorResp(String data, boolean isJsonArray) {
		try {
			if(isJsonArray) {
				jsonRespArrayErr = JSONArray.fromObject(data);
			}else {
				jsonRespErr = JSONObject.fromObject(data);
			}
		}catch(Exception e) {
			if (data.contains("Internal Server Error")) {
				jsonRespErr.put("message", "Internal Server Error");
			}
		}

		return new Exception(data);
	}

	private JSONObject onParseJsonObjectStr(String strResp) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object source, String name, Object value) {
				return (value == null);
			}
		});

		strResp = ((String) strResp).replace("null", "\"\"");
		return JSONObject.fromObject(strResp, jsonConfig);
	}



}

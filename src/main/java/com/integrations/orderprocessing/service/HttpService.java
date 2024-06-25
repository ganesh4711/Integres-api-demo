package com.integrations.orderprocessing.service;

import com.integrations.orderprocessing.model.req_body.optioryx.Barcodes;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.util.StringUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class HttpService {

	@Autowired
	WebClientService mWebClientService;
	
	@Autowired
	HttpClientService mHttpClientService;
	
	public JSONObject sendHttpGetRequest(String reqURL, String flowRefNum)  throws Exception {
		JSONObject resp =  mWebClientService.onSendGetRequest(reqURL);
		return resp;
	}
	
	public JSONObject sendHttpGetRequestWithTokenAuth(String reqURL, JSONObject reqPayLoad,String flowRefNum, String token)  throws Exception {
		JSONObject resp =  mWebClientService.onSendGetRequestWithTokenAuth(reqURL, token);
		return resp;
	}
	
	public JSONObject sendHttpGetRequestWithBasicAuth(String reqURL, String flowRefNum, String userName, String password)  throws Exception {
		JSONObject resp =  mWebClientService.onSendGetRequestWithBasicAuth(reqURL, userName, password);
		return resp;
	}
	
	public JSONObject sendHttpGetRequestWithBasicAuthByStringResp(String reqURL, String flowRefNum, String userName, String password)  throws Exception {
		JSONObject retResp = null;
		String resp =  mWebClientService.onSendGetRequestWithBasicAuthByStringResp(reqURL, userName, password);
		
		try {
		  resp = resp.substring(StringUtil.nthIndexOf(resp, "{", 2), resp.length());
		  retResp = JSONObject.fromObject(resp);
		}catch(Exception ex) {
			log.info("Exception in requesting FreightSnap service API:: {}", ex.getMessage());
			retResp = JSONObject.fromObject(resp);
		}
		
		log.info("SOAP JSON resp:: {}", resp);
		
		return retResp;
	}
	
	public JSONObject sendHttpPostRequest(String reqURL, JSONObject reqPayLoad,String flowRefNum)  throws Exception{
		JSONObject resp = mWebClientService.onSendPostRequest(reqURL, reqPayLoad);
		return resp;
	}
	
	public JSONObject sendHttpPostRequestWithTokenAuth(String reqURL, JSONObject reqPayLoad,String flowRefNum, String token)  throws Exception{
		JSONObject resp = mWebClientService.onSendPostRequestWithTokenAuth(reqURL, reqPayLoad, token);
		return resp;
	}
	
	public JSONObject sendHttpDeleteRequest(String reqURL, JSONObject reqPayLoad,String flowRefNum)  throws Exception{
		JSONObject resp = mWebClientService.onSendDeleteRequest(reqURL, reqPayLoad);
		return resp;
	}
	public JSONObject sendHttpGetRequestWithApikeyAuth(String reqURL, String Apikey) {
		JSONObject resp = mWebClientService.onSendGetRequestWithApiKeyAuth(reqURL, Apikey);
		return resp;
	}



	public JSONObject sendHttpGetRequestWithApikeyAuthandRequestBody(String reqURL, String apiKey, Barcodes barcodes) {
		JSONObject resp=mWebClientService.onSendPostRequestWithApiKeyAuthandRequestBody(reqURL,apiKey,barcodes);
		return resp;
	}

	public JSONObject sendHttpGetRequestWithApikey(String reqURL, String apiKey) {
		JSONObject i = mWebClientService.onSendGetRequestWithApiKey(reqURL, apiKey);
		return i;
	}

	public JSONObject sendHttpGetRequestWithApikeyAndPathvariable(String id, String reqURL, String apiKey) {
		JSONObject resp=mWebClientService.onSendGetRequestWithApiKeyAndPathVariable(id,reqURL,apiKey);
		return resp;
	}

	public JSONObject sendHttpPostRequestWithApikeyAuthandRequestBody(String reqURL, String apiKey, Barcodes barcodes) {
		JSONObject count= mWebClientService.onSendPostRequestWithApiKeyAndRequestbody(reqURL,apiKey,barcodes);
		return count;
	}
}

package com.integrations.orderprocessing.util;


import static com.integrations.orderprocessing.constants.HttpConstants.*;
import static com.integrations.orderprocessing.constants.StringConstants.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.integrations.orderprocessing.service.AuditDataService;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * @author Shiva Siyyadri
 *
 */
public class HttpUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	//private static final AuditDataService mAuditDataService = new AuditDataService();
    
	private String basicAuthKey;
	private String token;
	private boolean isBasicAuth;
	private boolean isBearerToken;
	private String flowRefNum;
	
	public HttpUtil(String flowRefNum) {
		this.flowRefNum = flowRefNum;
	}
	
    public HttpUtil(String authKey, String authType, String flowRefNum) {
    	this.flowRefNum = flowRefNum;
    	
    	switch(authType) {
    	case AUTH_TYPE_BASIC_AUTH:
    		  this.isBasicAuth=true;
    		  this.basicAuthKey = authKey;
    		break;
    	case AUTH_TYPE_BEARER_TOKEN:
    		this.isBearerToken=true;
    		this.token = authKey;
    		break;
    	}
	}

	public JSONObject getHttpResponse(String url, JSONObject reqBody, String reqType) {
    	JSONObject respObj = null;
    	String response = null;
    	String remarks = null;
    	
		try {
			
			switch(reqType) {
			case REQ_TYPE_GET:
				response = onSendGetRequest(url);
				break;
			case REQ_TYPE_POST:
				 response = onSendPostRequest(url, reqBody.toString());
				break;
			}
			
			if (response != null && !response.isEmpty()) {
				respObj = JSONObject.fromObject(response);
			}
			
		} catch (JSONException e) {
			remarks = "Exception in HttpUtil->getHttpResponse:: "+e.getMessage();
			logger.info(remarks);
			//mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
		}
		
		return respObj;
	}
    
	private String onSendPostRequest(String url, String reqObj) {
		String response = null;
		HttpURLConnection con = null;
		StringBuffer content = null;
		String remarks = null;
		
		logger.info("POST request being called...");
		
		try {
			byte[] postData = reqObj.getBytes(StandardCharsets.UTF_8);
			
			URL myurl = new URL(url);
			con = (HttpURLConnection) myurl.openConnection();
			
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			
			if(this.isBasicAuth) {
				con.setRequestProperty  ("Authorization", "Basic " + this.basicAuthKey);
			}else if (this.isBearerToken) {
            	con.setRequestProperty  ("Authorization", "Bearer " + this.token);
            }

			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(postData);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			String line;
			content = new StringBuffer();
			while ((line = br.readLine()) != null) {
				content.append(line)
					   .append(System.lineSeparator());
			}
			
			br.close();
		} catch (Exception e) {
			remarks = "Exception in HttpUtil->onSendPostRequest:: "+e.getMessage();
			logger.info(remarks);
			//mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
		} finally {
			if(con!=null)
			con.disconnect();
		}
		
		response = (content != null ? content.toString() : null);
		//logger.info("Response(onSendPostRequest):: "+response);
		
		return response;
	}
	
	 public String onSendGetRequest(String reqURL) {
		    String response = null;
		    HttpURLConnection con = null;
		    StringBuffer content = null;
		    String remarks = null;
			
		    logger.info("GET request being called...");
		    
	        try {
	            // Create URL object
	            URL url = new URL(reqURL);

	            // Open connection
	            con = (HttpURLConnection) url.openConnection();

	            // Set request method
	            con.setRequestMethod("GET");

	            // Set request headers if needed
				con.setRequestProperty("Accept", "application/json");
	            
	            if (this.isBearerToken) {
	            	con.setRequestProperty  ("Authorization", "Bearer " + this.token);
	            }
	            
	            // Get the response code
	            int responseCode = con.getResponseCode();
	            System.out.println("Response Code: " + responseCode);

	            // Read response body
	            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            
	            String inputLine;
	            content = new StringBuffer();
	            while ((inputLine = br.readLine()) != null) {
	            	content.append(inputLine)
	            		   .append(System.lineSeparator());
	            }
	            
	            br.close();
	        } catch (Exception e) {
	        	remarks = "Exception in HttpUtil->onSendGetRequest:: "+e.getMessage();
				logger.info(remarks);
				//mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
	        } finally {
	        	// Close connection
	        	if(con!=null)
	    			con.disconnect();
			}
	        
	        response = (content != null ? content.toString() : null);
	        //logger.info("Response(onSendGetRequest):: "+response);
	        
	        return response;
	    }
	
//	private String onSendPostRequest(String url, String reqObj) {
//		byte[] postData = reqObj.getBytes(StandardCharsets.UTF_8);
//		HttpsURLConnection con = null;
//		StringBuilder content = null;
//		try {
//
//			URL myurl = new URL(url);
//			con = (HttpsURLConnection) myurl.openConnection();
//			SSLContext sc = SSLContext.getInstance("SSL");
//			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom()); // Create
//																												// all-trusting
//																												// host
//																												// name
//																												// verifier
//			HostnameVerifier allHostsValid = new HostnameVerifier() {
//
//				@Override
//				public boolean verify(String hostname, SSLSession session) {
//					return true;
//				}
//			};
//			con.setHostnameVerifier(allHostsValid);
//
//			con.setDoOutput(true);
//			con.setRequestMethod("POST");
//			con.setRequestProperty("Content-Type", "application/json");
//
//			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//			wr.write(postData);
//
//			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//			String line;
//			content = new StringBuilder();
//
//			while ((line = br.readLine()) != null) {
//				content.append(line);
//				content.append(System.lineSeparator());
//			}
//			// System.out.println(content.toString());
//		 } catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			con.disconnect();
//		}
//
//		return (content != null ? content.toString() : null);
//	}
//	 
//	
//	public class TrustAnyTrustManager implements X509TrustManager {
//
//		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//		}
//
//		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//		}
//
//		public X509Certificate[] getAcceptedIssuers() {
//			return null;
//		}
//	}
	 
	
//	public static void main(String[] args) {
//		//String url = "https://idsandbox.nibss-plc.com.ng/oxauth/authorize.htm?scope=profile+bvn&acr_values=otp&response_type=code&redirect_uri=https://agentwallettest.xpresspayments.com/TermsandConditions/redirect.jsp&client_id=38fc9cab-d078-4f7b-b0ce-0d2a1bcd9c75";
//		String url = "https://agentwallettest.xpresspayments.com:7443/NibsBVNIgreeServiceImpl/v1/bvn-details";
//		JSONObject reqObj = new JSONObject();
//		reqObj.put("authcode", "46fe934f-c741-41f3-a9d2-fbe0677ef548");
////		reqObj.put("version", "V.1.0.0");
////		reqObj.put("channel", "MOBILE");
////		reqObj.put("requestType", "NONFIN");
//		
//		System.out.println(new HttpUtil().getHttpPostResponse(url, reqObj));
//	}
	
	
}

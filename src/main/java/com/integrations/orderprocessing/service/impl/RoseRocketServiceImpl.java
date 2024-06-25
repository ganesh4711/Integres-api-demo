package com.integrations.orderprocessing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.integrations.orderprocessing.constants.StringConstants;
import com.integrations.orderprocessing.model.payload.RRTokenBotristaPayload;
import com.integrations.orderprocessing.model.payload.RRTokenPayload;
import com.integrations.orderprocessing.model.payload.RRTokenSharpPayload;
import com.integrations.orderprocessing.model.req_body.roserocket.RRManifestAssignEvent;
import com.integrations.orderprocessing.primary_ds.entity.RRManifestAssignEventEntity;
import com.integrations.orderprocessing.secondary_ds.entity.EventLog;
import com.integrations.orderprocessing.service.AuditDataService;
import com.integrations.orderprocessing.service.HttpService;
import com.integrations.orderprocessing.service.RRManifestAssignEventService;
import com.integrations.orderprocessing.service.RoseRocketService;

import jakarta.annotation.PostConstruct;

import static com.integrations.orderprocessing.constants.RoseRocketConstants.*;
import static com.integrations.orderprocessing.util.ObjectUtil.*;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class RoseRocketServiceImpl implements RoseRocketService
{
	@Autowired
	ApplicationContext applicationContext;
	
	@Value("${app.rr.api.base_url}")
	private String rrBaseURL;
	
	@Value("${app.rr.api.token_url}")
	private String rrTokenURL;
	
	@Value("${app.rr.sharp.customer.id}")
	private String rrSharpCustId;
	
	@Value("${app.rr.botrista.customer.id}")
	private String rrBotristaCustId;
	
	@Autowired
	AuditDataService mAuditDataService;
	
	@Autowired
	HttpService mHttpService;
	
	@Autowired
	RRManifestAssignEventService rrManifestAssignEventService;
	
	@Override
	public EventLog onSaveManifestAssignEvent(RRManifestAssignEvent rrManifestAssignEvent, String orderId, String source, String flowRefNum) {
		return rrManifestAssignEventService.onSaveRRManifestAssignEvent(source, rrManifestAssignEvent, orderId);
	}

	@Override
	public String getRRToken(String rrCustomerType, String flowRefNum) {
		log.info("**** In getRRToken ****");
    	String token = null;
    	String remarks=null;
    	JSONObject reqBody =  null;
    	
    	RRTokenPayload  rrTokenPayload = getRRTokenPayloadByCustomerType(rrCustomerType);
    	
    	try {
			reqBody = JSONObject.fromObject(objectMapper.writeValueAsString(rrTokenPayload));
		} catch (JsonProcessingException e) {
			log.info("Exception in mapping RRTokenPayload to JSONObject ReqBody:: "+e.getMessage());
		}
    	
    	if(reqBody !=  null) {
    		remarks = new StringBuffer()
        			.append("RoseRocket-> Request URL(Token):: ")
        			.append(rrTokenURL).append(" ## Request Payload:: ")
        			.append(reqBody).toString();
        	log.info(remarks);
        	mAuditDataService.insertAuditData(remarks, flowRefNum, StringConstants.SUCCESS, StringConstants.INWARD);
        		
        	try {
        		JSONObject repObj = mHttpService.sendHttpPostRequest(rrTokenURL, reqBody, flowRefNum);
        		
    			if(repObj != null) {
    				JSONObject rrRepObj = (JSONObject)repObj.get("data");
    				if(rrRepObj != null) {
    				   token = (String)rrRepObj.get("access_token");
    				}
    			}
    		} catch (Exception e) {
    			log.info("Exception in getting RR Token:: "+e.getMessage());
    		}
    	}
    	
    	return token;
	}
	
	private RRTokenPayload getRRTokenPayloadByCustomerType(String type) {
		RRTokenPayload  rrTokenPayload = null;
		
		switch (type) {
		case RR_SHIPPER_TYPE_SHARP:
			rrTokenPayload = applicationContext.getBean(RRTokenSharpPayload.class);
			break;

		case RR_SHIPPER_TYPE_BOTRISTA:
			rrTokenPayload = applicationContext.getBean(RRTokenBotristaPayload.class);
			break;
			
		default:
			rrTokenPayload = applicationContext.getBean(RRTokenBotristaPayload.class);
			break;
		}
		
		return rrTokenPayload;
	}

	@Override
	public String getOrderIdByManifestId(String rrCustomerType, String manifestId, String flowRefNum, String token) {
    	String url = null;
    	String remarks = null;
    	JSONObject resp = null;
    	String orderId = null;
    	
    	log.info("**** In getOrderIdByManifestId ****");
    	
    	url = new StringBuffer()
    			.append(rrBaseURL)
    			.append("/manifests")
    			.append("/")
    			.append(manifestId)
    			.append("/legs").toString();
    	
    	remarks = new StringBuffer()
    			.append("RoseRocket-> Request URL(Get OrderId):: ")
    			.append(url).append(" ## Request Param:: ")
    			.append(manifestId).toString();
    	
    	log.info(remarks);
    	mAuditDataService.insertAuditData(remarks, flowRefNum, StringConstants.SUCCESS, StringConstants.INWARD);
    	
		if (token != null) {
			try {
				resp = mHttpService.sendHttpGetRequestWithTokenAuth(url, null, flowRefNum, token);
				if (resp != null) {
					JSONArray mJSONArray = resp.getJSONArray("legs");
					resp = (JSONObject) mJSONArray.get(0);
					orderId = resp.getString("order_id");
				}
			} catch (Exception e) {
				log.info("Exception in getting Order Id from RoseRocket by ManifestId:: "
						+ manifestId);
			}
		} else {
			log.info("RR Token should not be null");
		}
    	
    	return orderId;
	}

	@Override
	public String getPartnerCarrierIdByManifestId(String rrCustomerType, String manifestId, String flowRefNum, String token) {
    	String url = null;
    	String remarks = null;
    	JSONObject resp = null;
    	String carrierId = null;
    	String carrierServiceId = null;
    	
    	log.info("**** In getCarrierIdByManifestId ****");
    	
    	url = new StringBuffer()
    			.append(rrBaseURL)
    			.append("/manifests")
    			.append("/")
    			.append(manifestId)
    			.toString();
    	
    	remarks = new StringBuffer()
    			.append("RoseRocket-> Request URL(Get CarrierId):: ")
    			.append(url).append(" ## Request Param:: ")
    			.append(manifestId).toString();
    	
    	log.info(remarks);
    	mAuditDataService.insertAuditData(remarks, flowRefNum, StringConstants.SUCCESS, StringConstants.INWARD);
    	
    	if (token != null) {
			try {
				resp = mHttpService.sendHttpGetRequestWithTokenAuth(url, null, flowRefNum, token);
				if (resp != null) {
					resp = resp.getJSONObject("manifest");
					carrierId = resp.getString("partner_carrier_id");
					carrierServiceId = resp.getString("partner_carrier_service_id");
				}
			} catch (Exception e) {
				log.info("Exception in getting Order Id from " + rrCustomerType + " RoseRocket by ManifestId:: "
						+ manifestId);
			}
		} else {
			log.info("RR Token should not be null");
		}
    	
    	return carrierId;
	}

	@Override
	public JSONObject getPartnerCarrierDataByPartnerCarrierId(String rrCustomerType, String partnerCarrierId, String flowRefNum, String token) {
    	String url = null;
    	String remarks = null;
    	JSONObject resp = null;
    	
    	log.info("**** In getCarrierDataByCarrierId ****");
    	
    	url = new StringBuffer()
    			.append(rrBaseURL)
    			.append("/partner_carriers")
    			.append("/")
    			.append(partnerCarrierId).toString();
    	
    	remarks = new StringBuffer()
    			.append("RoseRocket-> Request URL(Get Partner Carrier Data):: ")
    			.append(url).append(" ## Request Param:: ")
    			.append(partnerCarrierId).toString();
    	
    	log.info(remarks);
    	mAuditDataService.insertAuditData(remarks, flowRefNum, StringConstants.SUCCESS, StringConstants.INWARD);
    	
    	if (token != null) {
			try {
				resp = mHttpService.sendHttpGetRequestWithTokenAuth(url, null, flowRefNum, token);
			} catch (Exception e) {
				log.info("Exception in getting Order Id from " + rrCustomerType + " RoseRocket by CarrierId:: "
						+ partnerCarrierId);
			}
		} else {
			log.info("RR Token should not be null");
		}
    	
    	return resp;
	}
	
	@Override
	public List<String> getAccessorialNamesByAccessorialIds(String rrCustomerType, List<String> accessorialIds, String flowRefNum, String token) {
		List<String> accessorialNames = new ArrayList<String>();
    	
		for(String accessorialId: accessorialIds) {
			String url = null;
	    	String remarks = null;
	    	JSONObject resp = null;
	    	
	    	log.info("**** In getAccessorialsByAccessorialId ****");
	    	
	    	url = new StringBuffer()
	    			.append(rrBaseURL)
	    			.append("/orgs")
	    			.append("/accessorials")
	    			.append("/")
	    			.append(accessorialId).toString();
	    	
	    	remarks = new StringBuffer()
	    			.append("RoseRocket-> Request URL(Get Accessorial Data):: ")
	    			.append(url).append(" ## Request Param:: ")
	    			.append(accessorialId).toString();
	    	
	    	log.info(remarks);
	    	mAuditDataService.insertAuditData(remarks, flowRefNum, StringConstants.SUCCESS, StringConstants.INWARD);
	    	
	    	if (token != null) {
				try {
					resp = mHttpService.sendHttpGetRequestWithTokenAuth(url, null, flowRefNum, token);
					resp = resp.getJSONObject("org_accessorial");
					accessorialNames.add(resp.getString("name"));
				} catch (Exception e) {
					log.info("Exception in getting Accessorials from " + rrCustomerType + " RoseRocket by AccessorialId:: "
							+ accessorialId);
				}
			} else {
				log.info("RR Token should not be null");
			}
	  }
    	
    	return accessorialNames;
	}

	@Override
	public JSONObject getOrderDataByOrderId(String rrCustomerType, String orderId, String flowRefNum, String token) {
    	String url = null;
    	String remarks = null;
    	JSONObject resp = null;
    	
    	log.info("**** In getOrderDataByOrderId ****");
    	
    	url = new StringBuffer()
    			.append(rrBaseURL)
    			.append("/orders")
    			.append("/")
    			.append(orderId).toString();
    	
    	remarks = new StringBuffer()
    			.append("RoseRocket-> Request URL(Get Order Data):: ")
    			.append(url).append(" ## Request Param:: ")
    			.append(orderId).toString();
    	
    	log.info(remarks);
    	mAuditDataService.insertAuditData(remarks, flowRefNum, StringConstants.SUCCESS, StringConstants.INWARD);
    	
    	if (token != null) {

			try {
				resp = mHttpService.sendHttpGetRequestWithTokenAuth(url, null, flowRefNum, token);
			} catch (Exception e) {
				log.info("Exception in getting Order Id from RoseRocket by OrderId:: "
						+ orderId);
			}
		} else {
			log.info("RR Token should not be null");
		}
    	
    	return resp;
	}
	
	@Override
	public String getCustomerIdByCustomerType(String type) {
		String customerId = null;
		
		if(type.equals(RR_SHIPPER_TYPE_SHARP)) {
			customerId = rrSharpCustId;
		} else if (type.equals(RR_SHIPPER_TYPE_BOTRISTA)) {
			customerId = rrBotristaCustId;
		}
		
		return customerId;
	}
	
	@Override
	public String getCustomerTypeByCustomerId(JSONObject orderData) {
		String customerType = "";
		String id = "";
		
		try {
			orderData = orderData.getJSONObject("order");
			JSONObject customerData = orderData.getJSONObject("customer");
			id = customerData.getString("id");
			
			log.info("Customer Id:: "+id);
		} catch (Exception e) {
			log.info("Exception in getting CustomerId");
		}
		
		if(id.equals(rrSharpCustId)) {
			customerType = RR_SHIPPER_TYPE_SHARP;
		} else if (id.equals(rrBotristaCustId)) {
			customerType = RR_SHIPPER_TYPE_BOTRISTA;
		}
		
		return customerType;
	}
}

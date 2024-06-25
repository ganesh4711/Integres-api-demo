package com.integrations.orderprocessing.controller;

import static com.integrations.orderprocessing.constants.HttpConstants.*;
import static com.integrations.orderprocessing.constants.ResponseConstants.*;
import static com.integrations.orderprocessing.constants.RoseRocketConstants.*;
import static com.integrations.orderprocessing.constants.StringConstants.FAILED;
import static com.integrations.orderprocessing.constants.StringConstants.INWARD;
import static com.integrations.orderprocessing.constants.StringConstants.SUCCESS;
import static com.integrations.orderprocessing.util.ObjectUtil.objectMapper;
import static com.integrations.orderprocessing.util.RoseRocketUtil.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.integrations.orderprocessing.dto.ResponseDTO;
import com.integrations.orderprocessing.model.req_body.roserocket.RRManifestAssignEvent;
import com.integrations.orderprocessing.service.AuditDataService;
import com.integrations.orderprocessing.service.HttpService;
import com.integrations.orderprocessing.service.RRManifestAssignEventService;
import com.integrations.orderprocessing.service.RoseRocketService;
import com.integrations.orderprocessing.service.StackEnableService;
import com.integrations.orderprocessing.util.AuditUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("${app.context.path}")
public class RoseRocketController {
	
	@Autowired
	AuditDataService mAuditDataService;
	
	@Autowired
	HttpService mHttpService;
	
	@Autowired
	RoseRocketService rrService;
	
	@Autowired
	RRManifestAssignEventService rrManifestAssignEventService;
	
	@Autowired
	StackEnableService stackEnableService;
	
	@PostMapping(value = "/onManifestAssignedByTest", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseDTO onManifestAssignedByTest(@Valid @RequestBody RRManifestAssignEvent rrManifestAssignEvent){
	   ResponseDTO response = new ResponseDTO();
	   String remarks= null;
		
       JSONObject reqJsonBody = null;
		
		try {
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(rrManifestAssignEvent));
		} catch (JsonProcessingException e) {
			log.info("Exception in request body convert to JSONObject:: "+e.getMessage());
		}
		
		remarks=(reqJsonBody != null)?"RequestBody(onManifestAssignedByTest):: "+reqJsonBody:remarks;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, "0", (reqJsonBody != null)?SUCCESS:FAILED, "");
		
		response.setMessage("Testing "
				+ SUCCESS
				+ "!");
		response.setCode(SUCCESS_CODE);
		response.setSuccess(true); 
		response.setRefNum("0");
		  
		return response;
    }
	
	@PostMapping(value = "/onManifestAssigned", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseDTO onManifestAssigned(@Valid @RequestBody RRManifestAssignEvent rrManifestAssignEvent){
		return onManifestAssignedByShipper(rrManifestAssignEvent, RR_SHIPPER_TYPE_DEFAULT);
    }
	
	private ResponseDTO onManifestAssignedByShipper(RRManifestAssignEvent rrManifestAssignEvent, String shipperType) {
		boolean flowStatus = false;	
		ResponseDTO response = new ResponseDTO();
		String remarks=null;
		
		
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum :: "+flowRefNum);
		response.setRefNum(flowRefNum);
		
		JSONObject reqJsonBody = null;
		
		try {
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(rrManifestAssignEvent));
		} catch (JsonProcessingException e) {
			remarks="Exception in request body convert to JSONObject:: "+e.getMessage();
		}
		
		remarks=(reqJsonBody != null)?"RequestBody(onManifestAssignedByBotrista):: "+reqJsonBody:remarks;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, "");
		
		if(reqJsonBody != null) {

			try {
					String manifestId = reqJsonBody.getString("manifest_id");
				
					remarks = "The Manifest Creation Flow Failed";
					
					String token = rrService.getRRToken(shipperType, flowRefNum);
					
					if(token != null) {
						String orderId = rrService.getOrderIdByManifestId(shipperType, manifestId, flowRefNum, token);
						JSONObject orderData = rrService.getOrderDataByOrderId(shipperType, orderId, flowRefNum, token);
						
						String realShipperType = rrService.getCustomerTypeByCustomerId(orderData);
						
						if(!realShipperType.isEmpty()) {
							// Save RR ManifestAssign with OrderId and CustomerId in Secondary DB
							rrManifestAssignEventService.onSaveRRManifestAssignEvent(realShipperType, rrManifestAssignEvent, orderId);
							
							String partnerCarrierId = rrService.getPartnerCarrierIdByManifestId(realShipperType, manifestId, flowRefNum, token);
							JSONObject partnerCarrierData = rrService.getPartnerCarrierDataByPartnerCarrierId(realShipperType, partnerCarrierId, flowRefNum, token);
							
							List<String> accessorialNames = rrService.getAccessorialNamesByAccessorialIds(realShipperType, getAccessorialsList(orderData), flowRefNum, token);
							
							// Send Order Data to StackEnable
							JSONObject resp = stackEnableService.onSendOrderDataToStackEnable(realShipperType, partnerCarrierData, orderData, accessorialNames, flowRefNum);
							
							if (resp != null) {
								flowStatus = true;
								remarks = "The Manifest Creation Flow Succeeded";
							}
						}else {
							remarks = "The Manifest Assign flow can not be processed for the Order Id:: "+orderId+". Since issue with customer Id.";	
						}
			      }
			} catch (Exception e) {
				remarks = "Exception in Manifest Assign event for the Shipper";
			}
		}
        
		log.info(remarks);

        if(flowStatus) {
			 response.setCode(SUCCESS_CODE);
			 response.setMessage(TOPIC_RESPONSE.replace("{topic}",MANIFEST));
			  response.setSuccess(flowStatus);
			  mAuditDataService.insertAuditData(response.getMessage(), flowRefNum, SUCCESS, INWARD);
        }else {
        	response.setMessage(remarks);
			response.setCode(FAILURE_CODE);
			response.setSuccess(flowStatus);  
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
        }
		
		return response;
	}
}

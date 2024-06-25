package com.integrations.orderprocessing.controller;

import jakarta.validation.Valid;

import static com.integrations.orderprocessing.constants.HttpConstants.*;
import static com.integrations.orderprocessing.constants.ResponseConstants.*;
import static com.integrations.orderprocessing.constants.StringConstants.*;
import static com.integrations.orderprocessing.util.ObjectUtil.objectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.integrations.orderprocessing.dto.AuthRequestDTO;
import com.integrations.orderprocessing.dto.FEResponseDTO;
import com.integrations.orderprocessing.dto.JwtResponseDTO;
import com.integrations.orderprocessing.dto.ResponseDTO;
import com.integrations.orderprocessing.model.req_body.fleetenable.FEAccountCreatedEvent;
import com.integrations.orderprocessing.model.req_body.fleetenable.FEAccountDeleteEvent;
import com.integrations.orderprocessing.model.req_body.fleetenable.FEAccountUpdatedEvent;
import com.integrations.orderprocessing.model.req_body.fleetenable.FEOrderData;
import com.integrations.orderprocessing.model.req_body.fleetenable.FEOrderDeletedEvent;
import com.integrations.orderprocessing.model.req_body.fleetenable.FEOrderSave;
import com.integrations.orderprocessing.model.req_body.fleetenable.FEOrgData;
import com.integrations.orderprocessing.model.req_body.fleetenable.FEwmsDataSave;
import com.integrations.orderprocessing.service.AuditDataService;
import com.integrations.orderprocessing.service.FleetEnableService;
import com.integrations.orderprocessing.service.HttpService;
import com.integrations.orderprocessing.service.JwtService;
import com.integrations.orderprocessing.service.StackEnableService;
import com.integrations.orderprocessing.util.AuditUtil;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;



@Tag(name="Integrations")
@ApiResponse(responseCode = "200", description = "Successful Response")
@ApiResponse(responseCode = "401", description = "Authentication Failure")
@ApiResponse(responseCode = "403", description = "Not Authorized")
@ApiResponse(responseCode = "400", description = "Bad request")
@ApiResponse(responseCode = "500", description = "Internal Server Error")

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("${app.context.path}/integrations/")
public class FleetEnableController {
	
	@Autowired
	FleetEnableService feService;
	
	@Autowired
	AuditDataService mAuditDataService;
	
	@Autowired
	HttpService mHttpService;
	
	@Autowired
	StackEnableService stackEnableService;
	
	@Autowired
    AuthenticationManager authenticationManager;
	
	@Autowired
	JwtService jwtService;
	
	@Operation(summary = "get Auth Token", description = "Authorize the resources by authenticate User details")
	@PostMapping(value="authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
	public JwtResponseDTO AuthenticateAndGetToken(@Valid @RequestBody AuthRequestDTO authRequestDTO){
	    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
	    if(authentication.isAuthenticated()){
	       return JwtResponseDTO.builder()
	               .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
	               .build();
	    } else {
	        throw new UsernameNotFoundException("Invalid user request..!!");
	    }
	}
	
	@Operation(summary = "send organization data", description = "Send Organization Data to StackEnable through Integrations")
	@PostMapping(value = "setup", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FEResponseDTO> onIntegrationSetup(@Valid @RequestBody FEOrgData feData){
		boolean flowStatus = false;	
		FEResponseDTO response = new FEResponseDTO();
		String remarks=null;
		
		String reqType = "onIntegrationSetup";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		try {
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(feData));
		} catch (JsonProcessingException e) {
			remarks="Exception in request body convert to JSONObject("
					+ reqType
					+ "):: "+e.getMessage();
		}
		
		remarks=(reqJsonBody != null)?"RequestBody("
				+ reqType
				+ "):: "+reqJsonBody:remarks;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, INWARD);
		
		if(reqJsonBody != null) {
			flowStatus = feService.onIntegrationSetup(feData, reqType, flowRefNum);	
		}
        
		ResponseEntity<FEResponseDTO> retResponse = null;
		if (flowStatus) {
			remarks = TOPIC_RESPONSE.replace("{topic}",FE_ORG_SETUP);
			response.setStatus("success");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			remarks = "Integration setup failed";
			response.setStatus("failed");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		return retResponse;
    }
	
	@Operation(summary = "create an account", description = "")
	@PostMapping(value = "onAccountCreated", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FEResponseDTO> onAccountCreated(@Valid @RequestBody FEAccountCreatedEvent feAccountCreatedEvent){
		boolean flowStatus = false;	
		FEResponseDTO response = new FEResponseDTO();
		String remarks=null;
		
		String reqType = "onAccountCreated";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		try {
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(feAccountCreatedEvent));
		} catch (JsonProcessingException e) {
			remarks="Exception in request body convert to JSONObject("
					+ reqType
					+ "):: "+e.getMessage();
		}
		
		remarks=(reqJsonBody != null)?"RequestBody("
				+ reqType
				+ "):: "+reqJsonBody:remarks;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, INWARD);
		
		if(reqJsonBody != null) {
			flowStatus = feService.onAccountCreated(reqJsonBody, reqType, flowRefNum);
		}
        
		ResponseEntity<FEResponseDTO> retResponse = null;
		if (flowStatus) {
			remarks = TOPIC_RESPONSE.replace("{topic}",FE_ACC_CREATE);
			response.setStatus("success");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			remarks = "The Account create failed";
			response.setStatus("failed");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		return retResponse;
    }
	
	
	@Operation(summary = "update an account", description = "")
	@PostMapping(value = "onAccountUpdated", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FEResponseDTO> onAccountUpdated(@Valid @RequestBody FEAccountUpdatedEvent feAccountUpdatedEvent){
		boolean flowStatus = false;	
		FEResponseDTO response = new FEResponseDTO();
		String remarks=null;
		
		String reqType = "onAccountUpdated";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		try {
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(feAccountUpdatedEvent));
		} catch (JsonProcessingException e) {
			remarks="Exception in request body convert to JSONObject("
					+ reqType
					+ "):: "+e.getMessage();
		}
		
		remarks=(reqJsonBody != null)?"RequestBody("
				+ reqType
				+ "):: "+reqJsonBody:remarks;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, INWARD);
		
		if(reqJsonBody != null) {
			flowStatus = feService.onAccountUpdated(reqJsonBody, reqType, flowRefNum);
		}
        
		ResponseEntity<FEResponseDTO> retResponse = null;
		if (flowStatus) {
			remarks = TOPIC_RESPONSE.replace("{topic}",FE_ACC_UPDATE);
			response.setStatus("success");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			remarks = "The Account update failed";
			response.setStatus("failed");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		return retResponse;
    }
	
	@Operation(summary = "delete an account", description = "")
	@DeleteMapping(value = "onAccountDeleted", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FEResponseDTO> onAccountDeleted(@Valid @RequestBody FEAccountDeleteEvent feAccountDeleteEvent){
		boolean flowStatus = false;	
		FEResponseDTO response = new FEResponseDTO();
		String remarks=null;
		
		String reqType = "onAccountDeleted";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		try {
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(feAccountDeleteEvent));
		} catch (JsonProcessingException e) {
			remarks="Exception in request body convert to JSONObject("
					+ reqType
					+ "):: "+e.getMessage();
		}
		
		remarks=(reqJsonBody != null)?"RequestBody("
				+ reqType
				+ "):: "+reqJsonBody:remarks;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, "");
		
		if(reqJsonBody != null) {
			flowStatus = feService.onAccountDeleted(reqJsonBody, reqType, flowRefNum);
		}
        
		ResponseEntity<FEResponseDTO> retResponse = null;
		if (flowStatus) {
			remarks = TOPIC_RESPONSE.replace("{topic}",FE_ACC_DELETE);
			response.setStatus("success");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			remarks = "The Account delete failed";
			response.setStatus("failed");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		return retResponse;
    }
	
	@Operation(summary = "create order", description = "")
	@PostMapping(value = "onOrderCreated", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FEResponseDTO> onOrderCreated(@Valid @RequestBody FEOrderData feOrderData){
		boolean flowStatus = false;	
		FEResponseDTO response = new FEResponseDTO();
		String remarks=null;
		
		String reqType="onOrderCreated";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		try {
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(feOrderData));
		} catch (JsonProcessingException e) {
			remarks="Exception in request body convert to JSONObject("
					+ reqType
					+ "):: "+e.getMessage();
		}
		
		remarks=(reqJsonBody != null)?"RequestBody("
				+ reqType
				+ "):: "+reqJsonBody:remarks;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, INWARD);
		
		if(reqJsonBody != null) {
			flowStatus = feService.onOrderCreated(feOrderData, reqType, flowRefNum);
		}
        
		ResponseEntity<FEResponseDTO> retResponse = null;
		if (flowStatus) {
			remarks = TOPIC_RESPONSE.replace("{topic}",FE_ORDER_CREATE);
			response.setStatus("success");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			remarks = "The Order create failed";
			response.setStatus("failed");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		return retResponse;
    }
	
	@Operation(summary = "update order", description = "")
	@PostMapping(value = "onOrderUpdated", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FEResponseDTO> onOrderUpdated(@Valid @RequestBody FEOrderData feOrderData){
		boolean flowStatus = false;	
		FEResponseDTO response = new FEResponseDTO();
		String remarks=null;
		
		String reqType="onOrderUpdated";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		try {
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(feOrderData));
		} catch (JsonProcessingException e) {
			remarks="Exception in request body convert to JSONObject("
					+ reqType
					+ "):: "+e.getMessage();
		}
		
		remarks=(reqJsonBody != null)?"RequestBody("
				+ reqType
				+ "):: "+reqJsonBody:remarks;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, INWARD);
		
		if(reqJsonBody != null) {
			flowStatus = feService.onOrderUpdated(feOrderData, reqType, flowRefNum);
		}
        
		ResponseEntity<FEResponseDTO> retResponse = null;
		if (flowStatus) {
			remarks = TOPIC_RESPONSE.replace("{topic}",FE_ORDER_UPDATE);
			response.setStatus("success");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			remarks = "The Order update failed";
			response.setStatus("failed");
			response.setMessage(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		return retResponse;
    }
	
	@Operation(summary = "delete order", description = "")
	@DeleteMapping(value = "onOrderDeleted", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FEResponseDTO> onOrderDeleted(@Valid @RequestBody FEOrderDeletedEvent feOrderDeletedEvent){
		boolean flowStatus = false;	
		FEResponseDTO response = new FEResponseDTO();
		String remarks=null;
		
		String reqType="onOrderDeleted";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		
		try {
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(feOrderDeletedEvent));
		} catch (JsonProcessingException e) {
			remarks="Exception in request body convert to JSONObject("
					+ reqType
					+ "):: "+e.getMessage();
		}
		
		remarks=(reqJsonBody != null)?"RequestBody("
				+ reqType
				+ "):: "+reqJsonBody:remarks;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, INWARD);
		
		if(reqJsonBody != null) {
			flowStatus = feService.onOrderDeleted(reqJsonBody, reqType, flowRefNum);
		}
        
		ResponseEntity<FEResponseDTO> retResponse = null;
        if(flowStatus) {
			remarks = TOPIC_RESPONSE.replace("{topic}",FE_ORDER_DELETE);
        	  response.setStatus( "success");
	    	  response.setMessage(remarks);
			  mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			  retResponse =  ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
        	remarks = "The Order delete failed";
        	response.setStatus( "failed");
	    	response.setMessage(remarks);  
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse =  ResponseEntity.status(HttpStatus.OK).body(response);
        }
		
		return retResponse;
    }
	
	// FleetEnable EndPoints to Share the StackEnable Data
	@Hidden
	@PostMapping(value = "onSendFleetEnableOrderData", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> onSendFleetEnableOrderData(@RequestBody FEOrderSave feOrderSave){
		boolean flowStatus = false;	
		ResponseDTO response = new ResponseDTO();
		String remarks=null;
		JSONObject retResp = null;
		
		String reqType = "onSendFleetEnableOrderData";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		try {
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(feOrderSave));
		} catch (JsonProcessingException e) {
			remarks="Exception in request body convert to JSONObject("
					+ reqType
					+ "):: "+e.getMessage();
		}
		
		remarks=(reqJsonBody != null)?"RequestBody("
				+ reqType
				+ "):: "+reqJsonBody:remarks;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, OUTWARD);
		
		if(reqJsonBody != null) {
			flowStatus = feService.onSendFleetEnableOrderData(reqJsonBody, reqType, flowRefNum);

		}
        
		ResponseEntity<ResponseDTO> retResponse = null;
		if (flowStatus) {

			remarks = TOPIC_RESPONSE.replace("{topic}",SE_ORDER_UPDATE);
			response.setRefNum(flowRefNum);
			response.setMessage(remarks);
			response.setCode(SUCCESS_CODE);
			response.setSuccess(true);
			
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			
			remarks = "Order data save failed:: ";
			response.setRefNum(flowRefNum);
			response.setMessage(remarks);
			response.setCode(FAILURE_CODE);
			response.setSuccess(false);
			
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		return retResponse;
    }
	
	@Hidden
	@PostMapping(value = "onSendWMSData", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> onSendWMSData(@RequestBody FEwmsDataSave FEwmsDataSave){
		boolean flowStatus = false;	
		ResponseDTO response = new ResponseDTO();
		String remarks=null;
		JSONObject retResp = null;
		
		String reqType = "onSendWMSData";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		try {
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(FEwmsDataSave));
		} catch (JsonProcessingException e) {
			remarks="Exception in request body convert to JSONObject("
					+ reqType
					+ "):: "+e.getMessage();
		}
		
		remarks=(reqJsonBody != null)?"RequestBody("
				+ reqType
				+ "):: "+reqJsonBody:remarks;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, OUTWARD);
		
		if(reqJsonBody != null) {
			flowStatus = feService.onSendFleetEnableWMSData(reqJsonBody, reqType, flowRefNum);
		}
        
		ResponseEntity<ResponseDTO> retResponse = null;
		if (flowStatus) {

			remarks = TOPIC_RESPONSE.replace("{topic}",WMS_DATA);
			response.setRefNum(flowRefNum);
			response.setMessage(remarks);
			response.setCode(SUCCESS_CODE);
			response.setSuccess(true);
			
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			
			remarks = "WMS data save failed:: ";
			response.setRefNum(flowRefNum);
			response.setMessage(remarks);
			response.setCode(FAILURE_CODE);
			response.setSuccess(false);
			
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		return retResponse;
    }
	
}

package com.integrations.orderprocessing.controller;

import static com.integrations.orderprocessing.constants.HttpConstants.FAILURE_CODE;
import static com.integrations.orderprocessing.constants.HttpConstants.SUCCESS_CODE;
import static com.integrations.orderprocessing.constants.StringConstants.FAILED;
import static com.integrations.orderprocessing.constants.StringConstants.INWARD;
import static com.integrations.orderprocessing.constants.StringConstants.OUTWARD;
import static com.integrations.orderprocessing.constants.StringConstants.SUCCESS;
import static com.integrations.orderprocessing.util.ObjectUtil.objectMapper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.integrations.orderprocessing.dto.DimScanResultDTO;
import com.integrations.orderprocessing.dto.ResponseDTO;
import com.integrations.orderprocessing.model.req_body.freightsnap.DimScanner;
import com.integrations.orderprocessing.model.req_body.freightsnap.DimScannerDateTime;
import com.integrations.orderprocessing.service.AuditDataService;
import com.integrations.orderprocessing.service.FreightSnapService;
import com.integrations.orderprocessing.util.AuditUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("${app.context.path}/FreightSnap/")
public class FreightSnapController {
  
	@Autowired
	FreightSnapService fsService;
	
	@Autowired
	AuditDataService mAuditDataService;
	
	@GetMapping(value = "getDimData", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> getDimensionerData(@RequestParam String proNumber, @RequestParam Optional<Double> weight, @RequestParam Optional<String> sendImage){
		boolean flowStatus = false;	
		ResponseDTO response = new ResponseDTO();
		String remarks=null;
		JSONObject retResp = null;
		DimScanner dimScanner = null;
		
		String reqType = "getDimensionerData";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		try {
			dimScanner = new DimScanner(proNumber, weight.get(), sendImage.get());
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(dimScanner));
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
			retResp = fsService.getDimensionerData(dimScanner, reqType, flowRefNum);
			flowStatus = retResp.getBoolean("status");
		}
        
		ResponseEntity<ResponseDTO> retResponse = null;
		if (flowStatus) {
			
			remarks = "Dimensions fetched successfully";
			response.setRefNum(flowRefNum);
			response.setMessage(remarks);
			response.setCode(SUCCESS_CODE);
			response.setSuccess(true);
			response.setData(retResp.get("data"));
			
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			
			remarks = "Dimensions fetch failed";
			response.setRefNum(flowRefNum);
			response.setMessage(remarks);
			response.setCode(FAILURE_CODE);
			response.setSuccess(false);
			response.setData(retResp.get("data"));
			
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		return retResponse;
    }
	
	@GetMapping(value = "getDimScanData", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> getDimScanData(@RequestParam String proNumber){
		boolean flowStatus = false;	
		ResponseDTO response = new ResponseDTO();
		String remarks=null;
		JSONObject retResp = null;
		DimScanner dimScanner = null;
		
		String reqType = "getDimScanData";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		try {
			dimScanner = new DimScanner();
			dimScanner.setProNumber(proNumber);	
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(dimScanner));
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
			retResp = fsService.getDimScanData(dimScanner, reqType, flowRefNum);
			flowStatus = retResp.getBoolean("status");
		}
        
		ResponseEntity<ResponseDTO> retResponse = null;
		if (flowStatus) {
			
			remarks = "Dimensions fetched successfully";
			response.setRefNum(flowRefNum);
			response.setMessage(remarks);
			response.setCode(SUCCESS_CODE);
			response.setSuccess(true);
			response.setData(retResp.get("data"));
			
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			
			remarks = "Dimensions fetch failed";
			response.setRefNum(flowRefNum);
			response.setMessage(remarks);
			response.setCode(FAILURE_CODE);
			response.setSuccess(false);
			response.setData(retResp.get("data"));
			
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		return retResponse;
    }
	
	@GetMapping(value = "getDateRangeDimScanData", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> getDateRangeDimScanData(@RequestParam String startDate, @RequestParam String startTime, @RequestParam String endDate, @RequestParam String endTime){
		boolean flowStatus = false;	
		ResponseDTO response = new ResponseDTO();
		String remarks=null;
		JSONObject retResp = null;
		DimScannerDateTime dimScannerDateTime = null;
		
		String reqType = "getDateRangeDimScanData";
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum("
				+ reqType
				+ "):: "+flowRefNum);
		
		JSONObject reqJsonBody = null;
		try {
			dimScannerDateTime = new DimScannerDateTime(startDate, startTime, endDate, endTime);
			reqJsonBody = JSONObject.fromObject(objectMapper.writeValueAsString(dimScannerDateTime));
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
			//flowStatus = true;
			retResp = fsService.getDateRangeDimScanData(dimScannerDateTime, reqType, flowRefNum);
			flowStatus = retResp.getBoolean("status");
		}
        
		ResponseEntity<ResponseDTO> retResponse = null;
		if (flowStatus) {
			
			remarks = "Dimensions fetched successfully";
			response.setRefNum(flowRefNum);
			response.setMessage(remarks);
			response.setCode(SUCCESS_CODE);
			response.setSuccess(true);
			response.setData(retResp.get("data"));
			
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			
			remarks = "Dimensions fetch failed";
			response.setRefNum(flowRefNum);
			response.setMessage(remarks);
			response.setCode(FAILURE_CODE);
			response.setSuccess(false);
			response.setData(retResp.get("data"));
			
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
			retResponse = ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		return retResponse;
    }
}

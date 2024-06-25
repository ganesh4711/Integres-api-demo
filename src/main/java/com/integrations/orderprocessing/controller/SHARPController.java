package com.integrations.orderprocessing.controller;

import static com.integrations.orderprocessing.constants.HttpConstants.FAILURE_CODE;
import static com.integrations.orderprocessing.constants.HttpConstants.SUCCESS_CODE;
import static com.integrations.orderprocessing.constants.StringConstants.FAILED;
import static com.integrations.orderprocessing.constants.StringConstants.OUTWARD;
import static com.integrations.orderprocessing.constants.StringConstants.SUCCESS;
import static com.integrations.orderprocessing.util.ObjectUtil.convertToJSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integrations.orderprocessing.dto.ResponseDTO;
import com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments.ProductAdjustmentDto;
import com.integrations.orderprocessing.model.req_body.inbound.gr.GoodsReceiptData;
import com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm.ShpmntConfInData;
import com.integrations.orderprocessing.service.AuditDataService;
import com.integrations.orderprocessing.service.WriteXMLToSHARPService;
import com.integrations.orderprocessing.util.AuditUtil;
import com.jcraft.jsch.Session;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${app.context.path}")
public class SHARPController {
	
	@Autowired
	AuditDataService auditDataService;
	
	@Autowired
	WriteXMLToSHARPService writeXMLToSHARPService;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	AuditDataService mAuditDataService;

	@Value("${app.enable.inward.write-xml}")
	private String isWriteXmlEnabled;
	
	@PostMapping(value = "/${SHARPController.sendGoodsReceipt}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> sendGoodsReceipt(@Valid @RequestBody GoodsReceiptData reqBody) {
		log.info("** SHARPController->sendGoodsReceipt Starts**");
		ResponseDTO response = new ResponseDTO();
		String remarks=null;
		
		String flowRefNum = AuditUtil.getRefNum();
		log.info("*** Flow RefNum :: "+flowRefNum+" ***");
		response.setRefNum(flowRefNum);
		
		String reqJsonBody = convertToJSONObject(reqBody);
		
		remarks="RequestBody(sendGoodsReceipt):: "+reqJsonBody;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks,
				flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, OUTWARD);
		
		if (reqJsonBody != null) {

				try {

					if (isWriteXmlEnabled.equalsIgnoreCase("yes")) {
						StringBuffer mStringBuffer = writeXMLToSHARPService.sharpOutboundToInboundDataMapper(reqBody,response, flowRefNum);
						
						if(!mStringBuffer.toString().isEmpty()) {
							remarks=mStringBuffer.toString();
						}else {
							remarks="File(s) saved successfully";
						}
						
					}else {
						remarks="File(s) save not enabled!!!";
					}
					
					mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, OUTWARD);
					log.info(remarks);
				} catch (Exception e) {
					remarks = "Exception while saving the GoodsReceipt file(s):: "+e.getMessage();
					log.info(remarks);
					
					mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, OUTWARD);
					response.setMessage(remarks);
					response.setCode(FAILURE_CODE);
				}
		} else {
			remarks = "Issue In request body(SHARPController->sendGoodsReceipt)";
			log.info(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, OUTWARD);
			response.setMessage("Issue In request body");
			response.setCode(FAILURE_CODE);
		}
		
		log.info("**SHARPController->sendGoodsReceipt Ends**");
		return  response.isSuccess() ? ResponseEntity.ok().body(response) : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response) ;
	}
	
	@PostMapping(value = "/${SHARPController.sendShipmentConfirm}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> sendShipmentConfirm(@Valid @RequestBody ShpmntConfInData reqBody) {
		log.info("** SHARPController->sendShipmentConfirm Starts **");
		
		ResponseDTO response = new ResponseDTO();
		String remarks=null;
		
		String flowRefNum = AuditUtil.getRefNum();
		log.info("Flow RefNum :: "+flowRefNum);
		response.setRefNum(flowRefNum);
		
		String reqJsonBody = convertToJSONObject(reqBody);
		
		remarks="RequestBody(sendShipmentConfirm):: "+reqJsonBody;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks,
				flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, OUTWARD);
		
		if (reqJsonBody != null) {
				try {
					
					if (isWriteXmlEnabled.equalsIgnoreCase("yes")) {
						StringBuffer mStringBuffer = writeXMLToSHARPService.sharpOutboundToInboundDataMapper(reqBody, response, flowRefNum);
						
						if(!mStringBuffer.toString().isEmpty()) {
							remarks=mStringBuffer.toString();
							response.setSuccess(false);
						}else {
							remarks="File saved successfully";
							response.setSuccess(true);
						}
					}else {
						remarks="File save not enabled!!!";
						response.setSuccess(true);
					}
					
					mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, OUTWARD);
					log.info(remarks);
					
				} catch (Exception e) {
					remarks = "Exception while saving the ShipmentConfirm file:: "+e.getMessage();
					log.info(remarks);
					
					mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, OUTWARD);
					response.setMessage(remarks);
					response.setCode(FAILURE_CODE);
				}

		} else {
			remarks = "Issue In request body(RestController->sendShipmentConfirm)";
			log.info(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, OUTWARD);
			response.setMessage("Issue In request body");
			response.setCode(FAILURE_CODE);
		}
		
		log.info("** SHARPController->sendShipmentConfirm Ends **");
		return  response.isSuccess() ? ResponseEntity.ok().body(response) : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response) ;
	}
	@RequestMapping(value = "/sendAdjustment", method = RequestMethod.POST , produces = "application/json")
	public ResponseEntity<ResponseDTO> sendSharpAdjustment(@Valid @RequestBody List<ProductAdjustmentDto> sharpAdjustmentsListDto){
		log.info("** SHARPController->sendSharpAdjustments Starts**");

		String remarks=null;
		String reqJsonBody = convertToJSONObject(sharpAdjustmentsListDto);
		remarks = "**sendSharpAdjustments RequestBody List::"+ reqJsonBody;
		log.info(remarks);

		ResponseDTO response = new ResponseDTO();
		String flowRefNum = AuditUtil.getRefNum();
		log.info("*** Flow RefNum :: "+flowRefNum+" ***");
		response.setRefNum(flowRefNum);
		response.setCode(FAILURE_CODE);
		mAuditDataService.insertAuditData(remarks,
				flowRefNum, (reqJsonBody != null)?SUCCESS:FAILED, OUTWARD);
		for (ProductAdjustmentDto adjustmentDto:sharpAdjustmentsListDto) {

			if (reqJsonBody != null) {

					try {

						if (isWriteXmlEnabled.equalsIgnoreCase("yes")) {
							StringBuffer mStringBuffer = writeXMLToSHARPService.sharpOutboundToInboundDataMapper(adjustmentDto, response, flowRefNum);

							if(!mStringBuffer.toString().isEmpty()) {
								remarks=mStringBuffer.toString();
							}else {
								remarks="File(s) saved successfully";
							}

						}else {
							remarks="File(s) save not enabled!!!";
						}
					} catch (Exception e) {
						remarks = "Exception while saving the file:: "+e.getMessage();
					}

			} else {
				remarks = "Issue In request body(sendSharpAdjustments)";
			}
		}

		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, OUTWARD);
		log.info("**SHARPController->sendSharpAdjustments Ends**");
		return response.isSuccess() ? ResponseEntity.ok().body(response) : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response) ;
	}
}

package com.integrations.orderprocessing.controller;

import com.integrations.orderprocessing.dto.ResponseDTO;
import com.integrations.orderprocessing.model.req_body.optioryx.Barcodes;
import com.integrations.orderprocessing.service.AuditDataService;
import com.integrations.orderprocessing.service.OptioryxService;
import com.integrations.orderprocessing.util.AuditUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.integrations.orderprocessing.constants.HttpConstants.FAILURE_CODE;
import static com.integrations.orderprocessing.constants.HttpConstants.SUCCESS_CODE;
import static com.integrations.orderprocessing.constants.StringConstants.*;


@Slf4j
@CrossOrigin
@RequestMapping("${app.context.path}/optioryx")
@RestController
public class OptioryxController {

    @Autowired
    private OptioryxService optioryxService;

    @Autowired
    AuditDataService mAuditDataService;


    @GetMapping(value = "/getItems")
    public ResponseEntity<ResponseDTO> fetchAllItems() {
        ResponseDTO response = new ResponseDTO();

        String remarks = null;
        JSONArray resp = null;
        boolean flowStatus = false;
        String reqType = "getItems";

        String flowRefNum = AuditUtil.getRefNum();
        log.info("Flow RefNum(" + reqType + "):: " + flowRefNum);

        JSONObject jsonResp = optioryxService.getAllItems();
        resp = (jsonResp.containsKey("data") ? jsonResp.getJSONArray("data") : null);
        remarks = jsonResp.getString("message");

        if (resp != null) {
            flowStatus = true;
        }


        if (flowStatus) {
            response.setRefNum(flowRefNum);
            response.setMessage(remarks);
            response.setCode(SUCCESS_CODE);
            response.setSuccess(true);
            response.setData(resp);
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
        } else {
            response.setRefNum(flowRefNum);
            response.setMessage(remarks);
            response.setCode(FAILURE_CODE);
            response.setSuccess(false);
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/getItemCount")
    public ResponseEntity<ResponseDTO> fetchItemCount() {
        ResponseDTO response = new ResponseDTO();
        String remarks = null;
        boolean flowStatus = false;
        String reqType = "getItemCount";
        String flowRefNum = AuditUtil.getRefNum();
        log.info("Flow RefNum(" + reqType + "):: " + flowRefNum);
        String resp = null;

        JSONObject jsonResp = optioryxService.getItemsCount();
        resp = (jsonResp.containsKey("data") ? jsonResp.getString("data") : null);
        remarks = jsonResp.getString("message");

        if (resp != null) {
            flowStatus = true;
        }

        if (flowStatus) {
            response.setRefNum(flowRefNum);
            response.setMessage(remarks);
            response.setCode(SUCCESS_CODE);
            response.setSuccess(true);
            response.setData(Integer.parseInt(resp));
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
        } else {
            response.setRefNum(flowRefNum);
            response.setMessage(remarks);
            response.setCode(FAILURE_CODE);
            response.setSuccess(false);
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/getItemById")
    public ResponseEntity<ResponseDTO> fetchItemById(@RequestParam("id") String id) {
        ResponseDTO response = new ResponseDTO();
        String remarks = null;
        JSONObject resp = null;
        boolean flowStatus = false;
        String reqType = "getItemById";
        String flowRefNum = AuditUtil.getRefNum();
        log.info("Flow RefNum(" + reqType + "):: " + flowRefNum);

        JSONObject jsonResp = optioryxService.getItemById(id);
        resp = (jsonResp.containsKey("data") ? (JSONObject) jsonResp.get("data") : null);
        remarks = jsonResp.getString("message");

        if (resp != null) {
            flowStatus = true;
        }
        if (flowStatus) {
            response.setRefNum(flowRefNum);
            response.setMessage(remarks);
            response.setCode(SUCCESS_CODE);
            response.setSuccess(true);
            response.setData(resp);
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
        } else {
            response.setRefNum(flowRefNum);
            response.setMessage(remarks);
            response.setCode(FAILURE_CODE);
            response.setSuccess(false);
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping(value = "/getItemsByBarcodes")
    public ResponseEntity<ResponseDTO> fetchItemByBarcodes(@RequestBody Barcodes barcodes) {
        ResponseDTO response = new ResponseDTO();
        String remarks = null;
        JSONArray resp = null;
        boolean flowStatus = false;
        String reqType = "getItemsByBarcodes";
        String flowRefNum = AuditUtil.getRefNum();
        log.info("Flow RefNum(" + reqType + "):: " + flowRefNum);

        JSONObject jsonResp = optioryxService.getItemByBarcodes(barcodes);
        resp = (jsonResp.containsKey("data") ? jsonResp.getJSONArray("data") : null);
        remarks = jsonResp.getString("message");

        if (resp != null) {
            flowStatus = true;
        }

        if (flowStatus) {
            response.setRefNum(flowRefNum);
            response.setMessage(remarks);
            response.setCode(SUCCESS_CODE);
            response.setSuccess(true);
            response.setData(resp);
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
        } else {
            response.setRefNum(flowRefNum);
            response.setMessage(remarks);
            response.setCode(FAILURE_CODE);
            response.setSuccess(false);
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/getItemCountByBarcodes")
    public ResponseEntity<ResponseDTO> fetchItemCountByBarcode(@RequestBody Barcodes barcodes) {
        ResponseDTO response = new ResponseDTO();
        String remarks = null;
        String resp = null;
        boolean flowStatus = false;
        String reqType = "getItemCountByBarcode";
        String flowRefNum = AuditUtil.getRefNum();
        log.info("Flow RefNum(" + reqType + "):: " + flowRefNum);


        JSONObject jsonResp = optioryxService.getItemCountByBarcode(barcodes);
        resp = (jsonResp.containsKey("data") ? jsonResp.getString("data") : null);
        remarks = jsonResp.getString("message");

        if (resp != null) {
            flowStatus = true;
        }


        if (flowStatus) {
            response.setRefNum(flowRefNum);
            response.setMessage(remarks);
            response.setCode(SUCCESS_CODE);
            response.setSuccess(true);
            response.setData(Integer.parseInt(jsonResp.getString("data")));
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
        } else {
            response.setRefNum(flowRefNum);
            response.setMessage(remarks);
            response.setCode(FAILURE_CODE);
            response.setSuccess(false);
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

package com.integrations.orderprocessing.service;

import static com.integrations.orderprocessing.constants.StringConstants.*;

import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm.ShpmntConfInData;
import com.integrations.orderprocessing.primary_ds.entity.ShipmentConfirmLog;
import com.integrations.orderprocessing.primary_ds.repository.ShipmentConfirmLogRepository;

import net.sf.json.JSONObject;

@Service
public class ShipmentConfirmLogService {
	
	@Autowired
	private ShipmentConfirmLogRepository mShipmentConfirmLogRepository;
	
	
    public JSONObject onCheckShipmentConfirmStatus(ShpmntConfInData objData) {
    	boolean status = true;
    	ShipmentConfirmLog retShipmentConfirmLog=null;
    			 
    	String shipmentNumber = objData.getShipmentNumber();
    	String deliveryNumber = objData.getDeliveryDetails().getDeliveryNumber();
		Optional<ShipmentConfirmLog> mShipmentConfirmLog = mShipmentConfirmLogRepository
															.getShipmentConfirmLog(shipmentNumber, deliveryNumber);
		if(mShipmentConfirmLog.isPresent()) {
			retShipmentConfirmLog = mShipmentConfirmLog.get();
    		status = false;
    	}else {
    		retShipmentConfirmLog = onSaveShipmentConfirmLog(objData);
    	}
    	
    	JSONObject jsonObj = new JSONObject();
    	jsonObj.put("status", status);
    	jsonObj.put("data", retShipmentConfirmLog.getId());
    	
    	return jsonObj;
	}
    
     private ShipmentConfirmLog onSaveShipmentConfirmLog(ShpmntConfInData srcObj) {
    	 ShipmentConfirmLog destObj = new ShipmentConfirmLog();
    	 destObj.setCreatedAt(new Date(System.currentTimeMillis()));
    	 destObj.setShipmentNumber(srcObj.getShipmentNumber());
    	 destObj.setDeliveryNumber(srcObj.getDeliveryDetails().getDeliveryNumber());
    	 destObj.setFlowType(OUTWARD);
    	 destObj.setStatus(false);
    	 
    	 return mShipmentConfirmLogRepository.save(destObj);
	}
     
     public int onUpdateFileNameInShipmentConfirmLog(Long scRecId, String fileName, boolean status) {
     	return mShipmentConfirmLogRepository.updateShipmentConfirmLog(scRecId, fileName, status);
     }
}

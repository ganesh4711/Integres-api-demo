package com.integrations.orderprocessing.service;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.model.req_body.inbound.gr.GRItem;
import com.integrations.orderprocessing.model.req_body.inbound.gr.GROrderDetails;
import com.integrations.orderprocessing.model.req_body.inbound.gr.GoodsReceiptData;
import com.integrations.orderprocessing.primary_ds.entity.GoodsReceiptLog;
import com.integrations.orderprocessing.primary_ds.repository.GoodsReceiptLogRepository;

import static com.integrations.orderprocessing.constants.SHARPConstants.*;
import static com.integrations.orderprocessing.constants.StringConstants.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GoodsReceiptLogService {

    @Autowired
    private GoodsReceiptLogRepository mGoodsReceiptLogRepository;
    
    @Autowired
    private PurchaseOrderMasterService mPurchaseOrderMasterService;
    
    public List<List<GRItem>> onCheckGoodsReceiptStatus(GoodsReceiptData goodsReceiptData) {
    	
    	List<List<GRItem>> sendList = new ArrayList<List<GRItem>>(2);
    	
    	List<GRItem> okList = new ArrayList<GRItem>();
    	List<GRItem> notOkList = new ArrayList<GRItem>();
    	
    	GROrderDetails orderDetails = goodsReceiptData.getOrderDetails();
    	String orderNum = orderDetails.getOrderNumber().trim();
    	for (GRItem item : orderDetails.getItems()){
    		
    		String itemNum = item.getItemNumber().trim();
    		
    		int newRcvdQuantity =  item.getQuantity();
    		int totalItemQuantity = (int) mPurchaseOrderMasterService.getOrderItemMasterQuantity(orderNum, itemNum);
    		
    		Optional<List<GoodsReceiptLog>> grLogList = mGoodsReceiptLogRepository.getGoodsReceiptLogList(orderNum, itemNum);
    		
    		if(grLogList.isPresent()) {
    			
    			int sentRcvdQuantity = grLogList.get().stream()
                .mapToInt(GoodsReceiptLog::getQuantity)
                .sum();
    			
    			if((newRcvdQuantity > 0) && (newRcvdQuantity <= (totalItemQuantity-sentRcvdQuantity))) {
    				onSaveGoodsReceiptLog(goodsReceiptData, item);
    				okList.add(item);
    			}else {
    				notOkList.add(item);
    			}
    			
    		}else {
    			
    			if((newRcvdQuantity > 0) && (newRcvdQuantity <= totalItemQuantity)) {
    				onSaveGoodsReceiptLog(goodsReceiptData, item);
    				okList.add(item);
    			}else {
    				notOkList.add(item);
    			}
    		}
    	}
    	
    	sendList.add(okList);
    	sendList.add(notOkList);
    	
    	return sendList;
	}
    
    private void onSaveGoodsReceiptLog(GoodsReceiptData goodsReceiptData,  GRItem grItem) {
   	 	GROrderDetails orderDetails = goodsReceiptData.getOrderDetails();
        
        GoodsReceiptLog goodsReceiptLog = new GoodsReceiptLog();
        
        goodsReceiptLog.setOrderNumber(orderDetails.getOrderNumber());
        goodsReceiptLog.setGoodsReceivedDate(goodsReceiptData.getOrderReceivedDate());
        goodsReceiptLog.setTrailerNumber(goodsReceiptData.getReferenceNumber());
        goodsReceiptLog.setItemNumber(grItem.getItemNumber());
        goodsReceiptLog.setProductId(grItem.getProductId());
        goodsReceiptLog.setPlantId(SHARP_WH_MAP_RCV.get(grItem.getPlantId().toUpperCase().trim()));
        goodsReceiptLog.setStorageLocation(grItem.getStorageLocation());
        goodsReceiptLog.setQuantity(grItem.getQuantity());
        goodsReceiptLog.setValuationType(SHARP_WH_MAP_RCV.get(grItem.getItemValuationType().toUpperCase().trim()));
        goodsReceiptLog.setFlowType(OUTWARD);
        goodsReceiptLog.setCreatedAt(new Date(System.currentTimeMillis()));
        goodsReceiptLog.setStatus(false);
        
        GoodsReceiptLog savedGoodsReceiptLog = mGoodsReceiptLogRepository.save(goodsReceiptLog);
        grItem.setGr_rec_id(savedGoodsReceiptLog.getId());
        grItem.setGrId(savedGoodsReceiptLog.getId());
	}
    
    public int onUpdateFileNameInGoodsReceiptLog(Long grRecId, String fileName, boolean status) {
    	return mGoodsReceiptLogRepository.updateGoodsReceiptLogBygrRecId(grRecId, fileName, status);
    }
}
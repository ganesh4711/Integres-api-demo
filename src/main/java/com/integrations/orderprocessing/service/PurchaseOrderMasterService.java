package com.integrations.orderprocessing.service;


import static com.integrations.orderprocessing.constants.SHARPConstants.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.primary_ds.entity.*;
import com.integrations.orderprocessing.primary_ds.repository.PurchaseOrderItemsMasterRepository;
import com.integrations.orderprocessing.primary_ds.repository.PurchaseOrderMasterRepository;

@Service
public class PurchaseOrderMasterService {

	@Autowired
	private PurchaseOrderMasterRepository mPurchaseOrderMasterRepository;
	
	@Autowired
	private PurchaseOrderItemsMasterRepository mOrderItemsMasterRepository;



	public Number getOrderItemMasterQuantity(String orderNum, String itemNum) {
		 Number existedQuantity = 0;
		 
		 Optional<PurchaseOrderMaster> mOrderMaster = mPurchaseOrderMasterRepository.findByOrderNumber(orderNum);
		 if(mOrderMaster.isPresent()) {
			 Number orderId = mOrderMaster.get().getId();
			 Optional<OrderItemMaster>  mOrderItemMaster = mOrderItemsMasterRepository.findByOrderRecIdAndItemNum(orderId, itemNum);
		     if(mOrderItemMaster.isPresent()) {
		    	 if(!mOrderItemMaster.get().getAction().equals(PO_ITEM_ACTION_CANCELLED_QLFR)) {
		    		 existedQuantity = mOrderItemMaster.get().getQuantity();
		    	 }
		     }
		 }
		 
		 return existedQuantity.intValue();
	}



}

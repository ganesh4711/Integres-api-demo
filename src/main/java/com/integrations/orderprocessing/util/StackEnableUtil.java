package com.integrations.orderprocessing.util;

import static com.integrations.orderprocessing.constants.RoseRocketConstants.RR_SHIPPER_TYPE_SHARP;
import static com.integrations.orderprocessing.constants.StringConstants.FAILED;
import static com.integrations.orderprocessing.constants.StringConstants.INWARD;
import static com.integrations.orderprocessing.constants.StringConstants.OUTWARD;
import static com.integrations.orderprocessing.constants.StringConstants.TRANSFER;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.integrations.orderprocessing.component.SharpShipperDetails;
import com.integrations.orderprocessing.model.req_body.stackenable.AddressStackEnable;
import com.integrations.orderprocessing.model.req_body.stackenable.OrderStackEnable;
import com.integrations.orderprocessing.secondary_ds.entity.Organization;
import com.integrations.orderprocessing.secondary_ds.repository.OrganizationRepository;
import com.integrations.orderprocessing.service.AuditDataService;
import com.integrations.orderprocessing.stackenable.enums.OrganizationType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StackEnableUtil {
	
	@Autowired
	SharpShipperDetails sharpShipperDetails;
	
	@Autowired
	OrganizationRepository orgRepository;
	
	@Autowired
	AuditDataService mAuditDataService;
	
	public String onGetOrderTypeAndSetWarehouseName(OrderStackEnable order, String shipperType, String flowRefNum) throws Exception{
		String orderType= null;
		String remarks = null;
		String orgnWareHouseName = null;
		String destWareHouseName = null;
		
		AddressStackEnable seOrgnAddress = order.getOriginAddress();
		AddressStackEnable seDestAddress = order.getDestinationAddress();
		    
		String orgnOrgName = (shipperType.equals(RR_SHIPPER_TYPE_SHARP)?sharpShipperDetails.wareHouseName:seOrgnAddress.getOrgName());
		String destOrgName = seDestAddress.getOrgName();
		
		Optional<Organization> originOrg = orgRepository.fetchOrganizationByName(orgnOrgName);
		Optional<Organization> destinationOrg = orgRepository.fetchOrganizationByName(destOrgName);
		
		if(originOrg.isPresent()) {
			OrganizationType originOrgType = originOrg.get().getOrganizationType();
			if(originOrgType.getValue() == 2) {
				orgnWareHouseName = orgnOrgName;
			}
		}
		
		if(destinationOrg.isPresent()) {
			OrganizationType destinationOrgType = destinationOrg.get().getOrganizationType();
			if(destinationOrgType.getValue() == 2) {
				destWareHouseName = destOrgName;
			}
		}
		
		if(orgnWareHouseName != null && destWareHouseName != null) {
			orderType = TRANSFER;
		}else if(orgnWareHouseName == null && destWareHouseName != null) {
			orderType = INWARD;
			order.setWareHouseName(destOrgName);
		}else if(orgnWareHouseName != null && destWareHouseName == null) {
			orderType = OUTWARD;
			order.setWareHouseName(orgnOrgName);
		}else {
			String suffix = " not found in StackEnable Database&";
			StringBuffer sb = new StringBuffer();
			remarks = "Origin Warehouse "+(orgnWareHouseName == null? "'"+ orgnOrgName + "' " + suffix:"");
			sb.append(remarks);
			
			log.info(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, null);
		
			remarks = "Destination Warehouse "+(destWareHouseName == null? "'"+ destOrgName + "' " + suffix:"");
			sb.append(remarks);
			
			log.info(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, null);
			
			remarks = sb.toString();
			remarks = (!remarks.isEmpty())?remarks.substring(0, remarks.length()-1):remarks;
		
			throw new RuntimeException(remarks);
		}
		
		return orderType;
	}
}

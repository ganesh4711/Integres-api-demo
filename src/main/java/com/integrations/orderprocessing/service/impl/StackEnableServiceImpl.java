package com.integrations.orderprocessing.service.impl;

import static com.integrations.orderprocessing.constants.RoseRocketConstants.RR_SHIPPER_TYPE_BOTRISTA;
import static com.integrations.orderprocessing.constants.RoseRocketConstants.RR_SHIPPER_TYPE_SHARP;
import static com.integrations.orderprocessing.constants.StringConstants.FAILED;
import static com.integrations.orderprocessing.constants.StringConstants.INWARD;
import static com.integrations.orderprocessing.constants.StringConstants.OUTWARD;
import static com.integrations.orderprocessing.constants.StringConstants.SUCCESS;
import static com.integrations.orderprocessing.constants.StringConstants.TRANSFER;
import static com.integrations.orderprocessing.constants.StringConstants.TRANSFER_INWARD;
import static com.integrations.orderprocessing.constants.StringConstants.TRANSFER_OUTWARD;
import static com.integrations.orderprocessing.util.ObjectUtil.objectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.integrations.orderprocessing.kafka.producer.KafkaAvroProducer;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.component.BotristaShipperDetails;
import com.integrations.orderprocessing.component.SharpShipperDetails;
import com.integrations.orderprocessing.constants.SHARPConstants;
import com.integrations.orderprocessing.model.req_body.stackenable.AddressStackEnable;
import com.integrations.orderprocessing.model.req_body.stackenable.InwardChildStackEnable;
import com.integrations.orderprocessing.model.req_body.stackenable.InwardRootStackEnable;
import com.integrations.orderprocessing.model.req_body.stackenable.ItemDetailsStackEnable;
import com.integrations.orderprocessing.model.req_body.stackenable.OrderInfoStackEnable;
import com.integrations.orderprocessing.model.req_body.stackenable.OrderStackEnable;
import com.integrations.orderprocessing.secondary_ds.entity.Organization;
import com.integrations.orderprocessing.secondary_ds.repository.OrganizationRepository;
import com.integrations.orderprocessing.service.AuditDataService;
import com.integrations.orderprocessing.service.HttpService;
import com.integrations.orderprocessing.service.RRManifestAssignEventService;
import com.integrations.orderprocessing.service.RoseRocketService;
import com.integrations.orderprocessing.service.StackEnableService;
import com.integrations.orderprocessing.stackenable.enums.OrganizationType;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class StackEnableServiceImpl implements StackEnableService{
	
	@Autowired
	OrganizationRepository orgRepository;
	
	@Autowired
	RoseRocketService rrService; 
	
	@Autowired
	HttpService httpService;
	
	@Autowired
	SharpShipperDetails sharpShipperDetails;
	
	@Autowired
	BotristaShipperDetails botristaShipperDetails;
	
	@Autowired
	RRManifestAssignEventService rrManifestAssignEventService;
	
	@Autowired
	AuditDataService mAuditDataService;
	@Autowired
	KafkaAvroProducer kafkaAvroProducer;
	
	@Value("${app.stack-enable.order-url}")
	private String orderUrl;

	@Override
	public JSONObject onSendOrderDataToStackEnable(String shipperType, JSONObject partnerCarrierData, JSONObject orderData, List<String> accessorialNames, String flowRefNum) {
		boolean flowStatus = false;
		String remarks = null;
		
		String url = new StringBuffer()
					.append(orderUrl)
					.toString();
		
		remarks = shipperType+ " Manifest Assign flow Request URL(ORDER):: "+url;
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, "");

		JSONObject reqJsonPayload = null;
		JSONObject resp = null;
		
		try {
			InwardRootStackEnable reqPayload = onFillOrderPayloadDtlsFromRRObj(partnerCarrierData, orderData, accessorialNames, shipperType, flowRefNum);
			
			reqJsonPayload = JSONObject.fromObject(objectMapper.writeValueAsString(reqPayload));
			
			remarks = shipperType+ " Manifest Assign flow Request Payload(ORDER):: "+reqJsonPayload;
			log.info(remarks);
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, "");

			kafkaAvroProducer.sendDataOnTopicStackEnbleOrder(reqPayload,flowRefNum);
			//resp = httpService.sendHttpPostRequest(url, reqJsonPayload, flowRefNum);
//			remarks = shipperType+ " Manifest Assign flow Response(ORDER):: "+resp;
			remarks = shipperType+ " Manifest Assign topic sent successfully.";

			
			flowStatus = true;
		} catch (Exception e) {
			remarks = "Exception in sending Manifest Assign Order Data To StackEnable for the Shipper:: "+shipperType+". Since "+e.getMessage();
		}
		
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, (flowStatus)?SUCCESS:FAILED, "");

		return resp;
	}
	
	private InwardRootStackEnable onFillOrderPayloadDtlsFromRRObj(JSONObject partnerCarrierData, JSONObject orderData, List<String> accessorialNames, String shipperType, String flowRefNum) throws Exception {
		
		partnerCarrierData = partnerCarrierData.getJSONObject("partner_carrier");
		orderData = orderData.getJSONObject("order");
		
		// Prepare Request Payload
		InwardRootStackEnable payLoad = new InwardRootStackEnable();
		List<InwardChildStackEnable> OrdersList = new ArrayList<InwardChildStackEnable>();
		payLoad.setOrders(OrdersList);
		
		InwardChildStackEnable mInwardChildStackEnable = new InwardChildStackEnable();
		OrdersList.add(mInwardChildStackEnable);
		
		// Stack Enable Order
		OrderStackEnable order = new OrderStackEnable();
		mInwardChildStackEnable.setOrder(order);
		
		order.setOrderId(orderData.getString("id"));
		order.setOrderNumber(orderData.getString("public_id"));
		order.setReferenceNumber(orderData.getString("ref_num"));
		
		order.setLegId(orderData.getString("current_leg_id"));
		order.setOrganizationName(onGetOrgNameType(shipperType)); // New
		order.setOrderStatus(SHARPConstants.ORDER_STATUS_NEW); // New
		
		// Method Call for Order Action Code
		onSetOrderActionCode(order, shipperType); // New
		
		order.setPartnerCarrierName(partnerCarrierData.getString("name")); // New
		order.setAccessorialNames(accessorialNames); // New
		order.setTag(accessorialNames.size()>0?"SWAP":"");
		
		// Change the details by Shipper
		onFillSharpShipperDetails(order, shipperType);
		
		// Origin Address
		AddressStackEnable orgnAddress = new AddressStackEnable();
		order.setOriginAddress(orgnAddress);
		JSONObject originAddress = orderData.getJSONObject("origin");
		
		// Method Call for mapping RR Origin Address to SEAddress
		onSetRRAddressToSEAddress(originAddress, orgnAddress);
		
		// Destination Address
		AddressStackEnable destAddress = new AddressStackEnable();
		order.setDestinationAddress(destAddress);
		JSONObject destinationAddress = orderData.getJSONObject("destination");
		
		// Method Call for mapping RR Destination Address to SEAddress
		onSetRRAddressToSEAddress(destinationAddress, destAddress);
		
		// Stack Enable OrderInfo
		OrderInfoStackEnable orderInfo = new OrderInfoStackEnable();
		mInwardChildStackEnable.setOrderInfo(orderInfo);
		
		orderInfo.setOrderDate(orderData.getString("created_at"));
		orderInfo.setPickupStartAt(orderData.getString("pickup_start_at"));
		orderInfo.setPickupEndAt(orderData.getString("pickup_end_at"));
		orderInfo.setShipmentNumber(orderData.getString("po_num"));
		orderInfo.setDeliveryNumber(orderData.getString("tender_num"));
		 
	     String orderType = onGetOrderTypeAndSetWarehouseName(order, shipperType, flowRefNum);
	     onSetCarrierAndShipperName(order, shipperType);
	     
	     // Stack Enable ItemDetails
		    List<ItemDetailsStackEnable> itemDetailsList = new ArrayList<ItemDetailsStackEnable>();
		    mInwardChildStackEnable.setItemDetails(itemDetailsList);
		    
		     for (int i=0; i< orderData.getJSONArray("commodities").size();i++) {
		    	
		    	JSONObject rrCommodity = orderData.getJSONArray("commodities").getJSONObject(i);
		    	
				if (orderType.equals(OUTWARD) || orderType.equals(TRANSFER)) {
					
					int quantity = rrCommodity.getInt("quantity");
					
					for (int q = 0; q < quantity; q++) {
						ItemDetailsStackEnable itemDetails = new ItemDetailsStackEnable();
						
						onSetStackEnableItemDetails(itemDetails, rrCommodity, false);
						
						itemDetailsList.add(itemDetails);
					}
				} else {
					ItemDetailsStackEnable itemDetails = new ItemDetailsStackEnable();

					onSetStackEnableItemDetails(itemDetails, rrCommodity, true);

					itemDetailsList.add(itemDetails);
				}
			}
	     
	     if(orderType.equals(TRANSFER)) {
	    	 order.setWareHouseName(orgnAddress.getOrgName());
	    	 order.setOrderType(TRANSFER_OUTWARD);
	    	 order.setOrderStatus(SHARPConstants.ORDER_STATUS_NEW); // New
	    	 
	    	 InwardChildStackEnable clonedOrder = (InwardChildStackEnable) SerializationUtils.clone(OrdersList.get(0));
	    	 clonedOrder.getOrder().setOrderType(TRANSFER_INWARD);
	    	 clonedOrder.getOrder().setWareHouseName(destAddress.getOrgName());
	    	 clonedOrder.getOrder().setOriginAddress(destAddress);
	    	 clonedOrder.getOrder().setDestinationAddress(orgnAddress);
	    	 clonedOrder.getOrder().setOrderStatus(SHARPConstants.ORDER_STATUS_BOOKED); // New
	    	 
	    	 OrdersList.add(clonedOrder);
	     }else {
	    	 order.setOrderType(orderType);
	     }
	     
	     
	    return payLoad;
	}
	
	private void onSetStackEnableItemDetails(ItemDetailsStackEnable itemDetails, JSONObject rrCommodity, boolean isInwardOrder) {
		itemDetails.setProductName(rrCommodity.getString("description"));
		itemDetails.setAction("002");
		itemDetails.setItemNumber("");
		itemDetails.setQuantity((isInwardOrder) ? rrCommodity.getInt("quantity") : 1);
		itemDetails.setDockLocation("");
		itemDetails.setValuationType("");
		itemDetails.setCommodityId(rrCommodity.getString("id"));
		itemDetails.setDescription(rrCommodity.getString("description"));

		itemDetails.setDimensions(onSetDimentions(rrCommodity));

		itemDetails.setFeet(rrCommodity.getDouble("feet"));
		itemDetails.setVolume(rrCommodity.getDouble("volume"));
		itemDetails.setMeasurementUnit(rrCommodity.getString("measurement_unit"));
		itemDetails.setWeightUnit(rrCommodity.getString("weight_unit"));
		itemDetails.setWeight(rrCommodity.getDouble("weight"));
		itemDetails.setSkuId(rrCommodity.getString("sku"));
	}
	
	private void onSetCarrierAndShipperName(OrderStackEnable order, String shipperType) {
		switch (shipperType) {
		case RR_SHIPPER_TYPE_SHARP: 
			order.setCarrierName(sharpShipperDetails.carrierName);
		    order.setShipperName(sharpShipperDetails.shipperName);
			break;
		case RR_SHIPPER_TYPE_BOTRISTA: 
			order.setCarrierName(botristaShipperDetails.carrierName);
		    order.setShipperName(botristaShipperDetails.shipperName);
			break;
		}
	}
	
	private String onGetOrderTypeAndSetWarehouseName(OrderStackEnable order, String shipperType, String flowRefNum) throws Exception{
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
	
	private String onSetDimentions(JSONObject item) {
		String retStr = "";
		
		try {
			retStr =  new StringBuffer()
					.append(item.getString("length"))
					.append("*")
					.append(item.getString("width"))
					.append("*")
					.append(item.getString("height"))
					.toString();
		} catch (Exception e) {
			log.info("Exception in getting Dimentions String");
		}
		
		
		return retStr;
	}
	
	private String onGetOrgNameType(String shipperType) {
		String source = "";
		switch (shipperType) {
		case RR_SHIPPER_TYPE_SHARP:
			source = sharpShipperDetails.carrierName;
			break;
		case RR_SHIPPER_TYPE_BOTRISTA:
			source = botristaShipperDetails.carrierName;
			break;
		}
		
		return source;
	}
	
	private void onSetOrderActionCode(OrderStackEnable seOrder, String shipperType) {
		switch (shipperType) {
		case RR_SHIPPER_TYPE_SHARP:
			seOrder.setActionCode("002");
			break;
		case RR_SHIPPER_TYPE_BOTRISTA:
			Optional<String> orderActionCode = rrManifestAssignEventService.getManifestAssignCountBySource(seOrder.getOrderId(), shipperType);
			if(orderActionCode.isPresent()) {
				seOrder.setActionCode(orderActionCode.get()); // New
			}
			break;
		}
	}
	
	private void onSetRRAddressToSEAddress(JSONObject inAddress, AddressStackEnable outAddress) {
		outAddress.setOrgName(inAddress.getString("org_name"));
		outAddress.setContactName(inAddress.getString("contact_name"));
		outAddress.setAddress1(inAddress.getString("address_1"));
		outAddress.setAddress2(inAddress.getString("address_2"));
		outAddress.setCity(inAddress.getString("city"));
		outAddress.setState(inAddress.getString("state"));
		outAddress.setCountry(inAddress.getString("country"));
		outAddress.setPostal(inAddress.getString("postal"));
		outAddress.setPhone(inAddress.getString("phone"));
		outAddress.setEmail(inAddress.getString("email"));
		outAddress.setLatitude(inAddress.getDouble("latitude"));
		outAddress.setLongitude(inAddress.getDouble("longitude"));
	}
	
	private void onFillSharpShipperDetails(OrderStackEnable order, String shipperType) {
		switch (shipperType) {
			case RR_SHIPPER_TYPE_SHARP:
				order.setCarrierName(sharpShipperDetails.carrierName);
				order.setWareHouseName(sharpShipperDetails.wareHouseName);
				order.setShipperName(sharpShipperDetails.shipperName);
				break;
	
			case RR_SHIPPER_TYPE_BOTRISTA:
				order.setCarrierName(botristaShipperDetails.carrierName);
				order.setWareHouseName(botristaShipperDetails.wareHouseName);
				order.setShipperName(botristaShipperDetails.shipperName);
				break;
		}
		
		order.setSource("RoseRocket");
	}

}

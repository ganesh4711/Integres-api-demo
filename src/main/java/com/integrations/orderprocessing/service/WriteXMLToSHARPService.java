package com.integrations.orderprocessing.service;

import net.sf.json.JSONObject;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.dto.ResponseDTO;
import com.integrations.orderprocessing.kafka.producer.KafkaAvroProducer;
//import com.integrations.orderprocessing.kafka.schema.GRItems;
import com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments.AdjustmentDetails;
import com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments.ProductAdjustment;
import com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments.ProductAdjustmentDto;
import com.integrations.orderprocessing.model.req_body.inbound.gr.GRItem;
import com.integrations.orderprocessing.model.req_body.inbound.gr.GROrderDetails;
import com.integrations.orderprocessing.model.req_body.inbound.gr.GoodsReceiptData;
import com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm.*;
import com.integrations.orderprocessing.model.xml.inbound.InventoryAdjustments.SHARPAdjustmentItemDetails;
import com.integrations.orderprocessing.model.xml.inbound.InventoryAdjustments.SharpAdjInboundRootEleDTO;
import com.integrations.orderprocessing.model.xml.inbound.InventoryAdjustments.SharpAdjustmentData;
import com.integrations.orderprocessing.model.xml.inbound.gr.SharpGRInboundRootEleDTO;
import com.integrations.orderprocessing.model.xml.inbound.gr.SharpGRItem;
import com.integrations.orderprocessing.model.xml.inbound.shipment_confirm.*;
import com.integrations.orderprocessing.primary_ds.entity.AdjustmentLog;
import com.integrations.orderprocessing.util.SFTPConnectionUtil;

import static com.integrations.orderprocessing.constants.HttpConstants.FAILURE_CODE;
import static com.integrations.orderprocessing.constants.HttpConstants.SUCCESS_CODE;
import static com.integrations.orderprocessing.constants.ResponseConstants.*;
import static com.integrations.orderprocessing.constants.SHARPConstants.*;
import static com.integrations.orderprocessing.constants.StringConstants.*;
import static com.integrations.orderprocessing.util.DateUtil.convertToDTTMtoDTTM;
import static com.integrations.orderprocessing.util.DateUtil.getCrntDTTMinReqFormat;

import java.util.*;
import java.util.stream.Collectors;



@Service
public class WriteXMLToSHARPService {

	private static final Logger logger = LoggerFactory.getLogger(WriteXMLToSHARPService.class);

	@Autowired
	KafkaAvroProducer kafkaAvroProducer;
	
	@Autowired
	AuditDataService mAuditDataService;
	
	@Autowired
	ProductMasterDataService mProductMasterDataService;
	
	@Autowired
	GoodsReceiptLogService mGoodsReceiptLogService;
	
	@Autowired
	ShipmentConfirmLogService mShipmentConfirmLogService;
	
	@Autowired
	ProcessedFileEntryService mProcessedFileEntryService;

	@Autowired
	AdjustmentLogService mAdjustmentLogService;
	
	@Value("${app.processed.file.save-in-db}")
	private String isFileNameSaveInDB;
	
	public StringBuffer sharpOutboundToInboundDataMapper(Object outboundData, ResponseDTO response, String flowRefNum) throws Exception {
		StringBuffer remarks = new StringBuffer("");
		response.setSuccess(false);
		response.setCode(FAILURE_CODE);
		if (outboundData != null && outboundData instanceof GoodsReceiptData) {
			
			GoodsReceiptData objData = (GoodsReceiptData) outboundData;
			remarks.append("The Order Number:: "+objData.getOrderDetails().getOrderNumber());
			
			List<List<GRItem>> receiveList =mGoodsReceiptLogService.onCheckGoodsReceiptStatus(objData);
			
			List<GRItem> okList = receiveList.get(0);
	    	List<GRItem> notOkList = receiveList.get(1);
			
	    	if(!okList.isEmpty() && notOkList.isEmpty()) {
	    		
	    		GROrderDetails mGROrderDetails = objData.getOrderDetails();
	    		mGROrderDetails.setItems(okList);
	    		
	    		ModelMapper modelMapper = new ModelMapper();
    			com.integrations.orderprocessing.kafka.schema.GoodsReceiptData grSchemaData = modelMapper.map(objData, com.integrations.orderprocessing.kafka.schema.GoodsReceiptData.class);
    			
	    		kafkaAvroProducer.sendDataOnTopicStackEnbleGR(objData,flowRefNum);

				response.setSuccess(true);
				response.setCode(SUCCESS_CODE);
				response.setMessage(TOPIC_RESPONSE.replace("{topic}",GOODS_RECEIPT));
	    		
				remarks.append("#");
				for (GRItem mGRItem : okList) {
					remarks.append("@Goods Receipt has been sent with Quantity:: "+mGRItem.getQuantity()
    		  		+ " for the productId:: "+mGRItem.getProductId());
				}

		  }
	      
	      if(notOkList.size()>0){
	    	  remarks.append("#");
	    	  for (GRItem mGRItem : notOkList) {
	    		  remarks.append("@Having issue with Not Found or Quantity:: "+mGRItem.getQuantity()
	    		  		+ " for the productId:: "+mGRItem.getProductId());
	    	  }
		  }
	      
	      if(okList.size()==0 && notOkList.size()==0) {
			  remarks.append("#No Items found in GoodsReceipt to send");
		  }
	      
		} else if (outboundData != null && outboundData instanceof ShpmntConfInData) {
			
			ShpmntConfInData objData = (ShpmntConfInData) outboundData;
			
			JSONObject jsonObj = mShipmentConfirmLogService.onCheckShipmentConfirmStatus(objData);
			boolean status = (jsonObj != null ? jsonObj.getBoolean("status") : false);
			Long scRecId = (jsonObj != null ? (Long)jsonObj.getLong("data"): -1);
			
			objData.setSc_rec_id(scRecId);
			objData.setScId(scRecId);
			
			if(status) 
			{
				
				objData.getDeliveryDetails().setPartnerDetails(new PartnerDetails());
				ModelMapper modelMapper = new ModelMapper();
				com.integrations.orderprocessing.kafka.schema.ShpmntConfInData scSchema = modelMapper.map(objData, com.integrations.orderprocessing.kafka.schema.ShpmntConfInData.class);
				
				kafkaAvroProducer.sendDataOnTopicStackEnbleSC(objData,flowRefNum);
				response.setSuccess(true);
				response.setCode(SUCCESS_CODE);
				response.setMessage(TOPIC_RESPONSE.replace("{topic}",SHIPMENT_CONFIRMATION));
				
			}else {
		    	remarks.append("Shipment Confirmation has already been sent with given ShipmentNumber:: ")
		    	.append(objData.getShipmentNumber())
		    	.append(" and ").append("DeliveryNumber:: ")
		    	.append(objData.getDeliveryDetails().getDeliveryNumber());
		    }
		}else if (outboundData != null && outboundData instanceof ProductAdjustmentDto adjData) {

				List<Map<Long, Object>> sendObjList = onAdjustmentDataToInboundDataMapper(adjData, remarks);
			    kafkaAvroProducer.sendDataOnTopicStackEnbleAdj(adjData,flowRefNum);
			response.setSuccess(true);
			response.setCode(SUCCESS_CODE);
			response.setMessage(TOPIC_RESPONSE.replace("{topic}",ADJUSTMENT));
			}
			return remarks;
		}

	private List<Map<Long, Object>> onAdjustmentDataToInboundDataMapper (ProductAdjustmentDto adjData, StringBuffer remarks){

			String movementDate = convertToDTTMtoDTTM(adjData.getAdjustmentDate(), DATE_PATTERN2, DATE_PATTERN);
		    AdjustmentDetails mAdjustmentDetails = adjData.getAdjustmentDetails();

		    SharpAdjInboundRootEleDTO sendObj = new SharpAdjInboundRootEleDTO();

			SharpAdjustmentData sharpAdjustmentData = sendObj.getIdoc().getAdjustmentData();
	    	SHARPAdjustmentItemDetails sharpAdjustmentItemDetails = sharpAdjustmentData.getAdjustmentItemDetails();


		List<Map<Long, Object>> sendObjList = new ArrayList<Map<Long, Object>>();

			sendObj.getIdoc().getEdi_dc40().setDocnum(getCrntDTTMinReqFormat(DATE_TIME_PATTERN));

			sharpAdjustmentData.setGoodsMovementDate1(movementDate);
			sharpAdjustmentData.setGoodsMovementDate2(movementDate);
			sharpAdjustmentData.setReferenceNumber(adjData.getReferenceNumber());
			sharpAdjustmentData.setAdjustmentNumber(adjData.getReferenceNumber());
			sharpAdjustmentData.setNote(adjData.getNote());

			// set adjustment item details
			onSetFromProductDetails(mAdjustmentDetails,sharpAdjustmentItemDetails);

			String movementType = adjData.getAdjustmentDetails().getMovementType();
			sharpAdjustmentItemDetails.setMovementType(movementType);
			sharpAdjustmentItemDetails.setQuantity(mAdjustmentDetails.getQuantity());

			remarks.append("@Adjustment has been sent with Quantity:: ").append(sharpAdjustmentItemDetails.getQuantity())
					.append(" for productId:: ").append(sharpAdjustmentItemDetails.getProductId());
			if (!sharpAdjustmentItemDetails.getValuationType().isEmpty()) {
				remarks.append(" valuationType:: ").append(sharpAdjustmentItemDetails.getValuationType());
			}


		ProductAdjustment mToProductData = null;
		switch (movementType) {
			case GM_ADJUSTMENT_701:
			case GM_ADJUSTMENT_702:
				validateToProductIsNull(mAdjustmentDetails);
				sharpAdjustmentData.setTransactionCode(GM_TRANSACTION_CODE_MI07);
				break;
			case GM_ADJUSTMENT_309:
			case GM_ADJUSTMENT_322:
			case GM_ADJUSTMENT_321:
				validateToProductIsNotNull(mAdjustmentDetails);
				mToProductData = mAdjustmentDetails.getToProduct();;
				onSetToProductDetails(mToProductData, sharpAdjustmentItemDetails, remarks);
				sharpAdjustmentItemDetails.setReceivingLocation(mToProductData.getStorageLocation());
				sharpAdjustmentData.setTransactionCode(GM_TRANSACTION_CODE_MIGO);
				break;
			case GM_ADJUSTMENT_311:
				validateToProductIsNotNull(mAdjustmentDetails);
				mToProductData = mAdjustmentDetails.getToProduct();;
				onSetToProductDetails(mToProductData, sharpAdjustmentItemDetails, remarks);
				sharpAdjustmentItemDetails.setReceivingLocation(GM_SLOC_QLFR_RESV);
				sharpAdjustmentData.setTransactionCode(GM_TRANSACTION_CODE_MIGO);
				break;
			default:
				throw new RuntimeException("Invalid movement type: " + movementType);
		}
			Long fakeId = 0L;
			sendObjList.add(new HashMap<>() {{
				put(fakeId, sendObj);
			}});
			return sendObjList;
		}
	private void validateToProductIsNull(AdjustmentDetails mAdjustmentDetails) {
		if (mAdjustmentDetails.getToProduct() != null) {
			throw new RuntimeException("To product details must be null for movement type " + mAdjustmentDetails.getMovementType());
		}
	}
	private void validateToProductIsNotNull(AdjustmentDetails mAdjustmentDetails) {
		if (mAdjustmentDetails.getToProduct() == null) {
			throw new RuntimeException("To product details must not be null for movement type " + mAdjustmentDetails.getMovementType());
		}
	}

	private void onSetFromProductDetails(AdjustmentDetails mAdjustmentDetails, SHARPAdjustmentItemDetails sharpAdjustmentItemDetails) {

		ProductAdjustment mFromProdDetails = mAdjustmentDetails.getFromProduct();
		String mFromProductId = mFromProdDetails.getProductId();

		sharpAdjustmentItemDetails.setProductId(mFromProductId);
		sharpAdjustmentItemDetails.setStorageLocation(mFromProdDetails.getStorageLocation());

		String valuationType = SHARP_WH_MAP_RCV.getOrDefault(mFromProdDetails.getItemValuationType().toUpperCase(), "");
		if (!valuationType.isBlank()) {
			sharpAdjustmentItemDetails.setValuationType(valuationType);
		}

		String plantId = SHARP_WH_MAP_RCV.getOrDefault(mFromProdDetails.getPlantId().toUpperCase(), "");
		sharpAdjustmentItemDetails.setPlantId(plantId);
	}

	private void onSetToProductDetails(ProductAdjustment mToProdDetails, SHARPAdjustmentItemDetails sharpAdjustmentItemDetails, StringBuffer remarks) {

		String mFromProductId = sharpAdjustmentItemDetails.getProductId();
		String movementType = sharpAdjustmentItemDetails.getMovementType();

		String mToProductId = mToProdDetails.getProductId();

		if (!mFromProductId.equals(mToProductId)) {
			throw new RuntimeException("Invalid to_product details::" + mToProductId);
		}
		sharpAdjustmentItemDetails.setReceivingProductId(mToProductId);

		String rcvPlantId = SHARP_WH_MAP_RCV.getOrDefault(mToProdDetails.getPlantId().toUpperCase(), "");
		sharpAdjustmentItemDetails.setReceivingPlantId(rcvPlantId);

		String fromProdValuationType = sharpAdjustmentItemDetails.getValuationType();
		String rcvValuationType = SHARP_WH_MAP_RCV.getOrDefault(mToProdDetails.getItemValuationType().toUpperCase(), "");

		if (!movementType.equals(GM_ADJUSTMENT_309) && !fromProdValuationType.equals(rcvValuationType)){
            throw new RuntimeException("Product valuation types must be same for movement type:: "+movementType);
        }
		sharpAdjustmentItemDetails.setReceivedItemValuationType(rcvValuationType);

		remarks.append(" to productId:: ").append(sharpAdjustmentItemDetails.getReceivingProductId());
		if (!sharpAdjustmentItemDetails.getReceivedItemValuationType().isEmpty()) {
			remarks.append(" valuationType:: ").append(sharpAdjustmentItemDetails.getReceivedItemValuationType());
		}
	}
}


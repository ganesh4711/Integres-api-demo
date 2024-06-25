package com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode.Include;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryDetails {
	
	@NotBlank(message = "Delivery Number cannot be blank")
	private String deliveryNumber;
	
	@NotBlank(message = "Plant Id cannot be blank")
	private String plantId;
	
	@NotBlank(message = "Storage Location cannot be blank")
	private String storageLocation;
	
	private float totalWeight;
	private int totalPackagesInDelivery;

	@NotNull(message = "Carrier Tracking Details cannot be null")
	private CarrierTrkgDlvry carrierTrackingDetails;
	
	private PartnerDetails partnerDetails;
	
	@Valid
	@NotNull(message = "Goods Issue to Delivery details cannot be null")
	private ShpmentConfInGoodsActvtsIssueToDlvry goodsIssueToDelivery;
	
	@Valid
	@NotNull(message = "Carrier Tracking Details cannot be null")
	private List<ShpmntConfInItem> shipmentConfirmationItems;
}
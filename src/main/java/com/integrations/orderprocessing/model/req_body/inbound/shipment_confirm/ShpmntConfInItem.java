package com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShpmntConfInItem {
	
	@NotBlank(message = "Product Id cannot be blank")
	private String productId;
	
	private int quantity;
	
	@NotBlank(message = "Item Number cannot be blank")
	private String itemNumber;
	
	@NotBlank(message = "Delivery Status cannot be blank")
	private String deliveryStatus;
	
	@Valid
	@NotNull(message = "Serial Numbers list cannot be null")
	private List<String> serialNumbers;
	
	@Valid
	@NotNull(message = "Item Label Details list cannot be null")
	private List<ItemLabelDetails> itemLabelDetailsList;
}

package com.integrations.orderprocessing.model.req_body.inbound.gr;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GoodsReceiptData {

	private String docNum;
	
	@NotBlank(message = "Order Received Date cannot be blank")
	private String orderReceivedDate;
	
	@NotBlank(message = "Reference Number cannot be blank")
	private String referenceNumber;
	
	@Valid
	@NotNull(message = "GR Order Details cannot be null")
	private GROrderDetails orderDetails;
}
package com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm;


import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShpmntConfInData {
	
	private Long scId;
	
	private Long sc_rec_id;
	
	private String docNum;

	@NotBlank(message = "Shipment Number cannot be blank")
	private String shipmentNumber;
	
	@NotBlank(message = "BOL Number cannot be blank")
	private String bolNumber;
	
	@Valid
	@NotNull(message = "Delivery details cannot be null")
	private DeliveryDetails deliveryDetails;
}
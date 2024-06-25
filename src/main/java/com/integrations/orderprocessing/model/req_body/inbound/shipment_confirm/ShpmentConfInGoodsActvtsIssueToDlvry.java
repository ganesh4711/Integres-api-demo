package com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShpmentConfInGoodsActvtsIssueToDlvry {
	
	@NotBlank(message = "Shipped Delivery Date cannot be blank")
	private String shippedDeliveryDate;
}

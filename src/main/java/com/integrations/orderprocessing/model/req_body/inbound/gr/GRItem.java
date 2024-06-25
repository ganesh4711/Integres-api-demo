package com.integrations.orderprocessing.model.req_body.inbound.gr;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GRItem {
	
	private Long grId;

	private Long gr_rec_id;

	@NotBlank(message = "Product Id cannot be blank")
	private String productId;

	@Min(value = 1, message = "Quantity must be greater than 0")
	private int quantity;

	@NotBlank(message = "Item Number cannot be blank")
	private String itemNumber;

	private String itemValuationType;

	@NotBlank(message = "Plant Id cannot be blank")
	private String plantId;

	@NotBlank(message = "Storage Location cannot be blank")
	private String storageLocation;
}
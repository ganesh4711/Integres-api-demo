package com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class ProductAdjustment {
    @NotBlank(message = "Product id cannot be blank")
    private String productId;
    @NotBlank(message = "Plant id cannot be blank")
    private String plantId;
    @NotNull(message = "storage location cannot be null")
    private String storageLocation;
    @NotBlank(message = "Product valuation cannot be blank")
    private String itemValuationType;
}

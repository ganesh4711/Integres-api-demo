package com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AdjustmentDetails {

    @Valid
    @NotNull(message = "Product details cannot be null")
    private ProductAdjustment fromProduct;

    @NotBlank(message = "Quantity cannot be blank")
    private String quantity;

    @NotBlank(message = "Movement type cannot be blank")
    private String movementType;
    @Valid
    private ProductAdjustment toProduct;
}

package com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductAdjustmentDto {
    @NotBlank(message = "Adjustment date cannot be blank")
    private String adjustmentDate;              // date by stackEnable
    @NotBlank(message = "reference number cannot be blank")
    private String referenceNumber;             // current timestamp
    @NotNull(message = "note cannot be null")
    private String note;
    @Valid
    @NotNull(message = "Adjustment details cannot be null")
    private AdjustmentDetails adjustmentDetails;

}

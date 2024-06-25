package com.integrations.orderprocessing.model.req_body.inbound.gr;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GROrderDetails {

    @NotBlank(message = "Order Number cannot be blank")
    private String orderNumber;
    
    @Valid
    @NotNull(message = "Items cannot be null")
    private List<GRItem> items;
}
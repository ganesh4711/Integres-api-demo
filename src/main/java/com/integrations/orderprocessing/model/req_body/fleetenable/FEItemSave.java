package com.integrations.orderprocessing.model.req_body.fleetenable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEItemSave {
    private String reference;
    private String purchase_order_number;
    private String weight_qualifier;
    private double weight;
    private int quantity;
    private String uom;
    private double length;
    private double width;
    private double height;
    private String volume_qualifier;
    private String volume;
    private String description;
}

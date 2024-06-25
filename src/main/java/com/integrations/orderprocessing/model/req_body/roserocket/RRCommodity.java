package com.integrations.orderprocessing.model.req_body.roserocket;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RRCommodity {
	private String id;
    private String name;
    private String measurement_unit;
    private String weight_unit;
    private String freight_class;
    private String commodity_type;
    private String description;
    private double feet;
    private double volume;
    private double length;
    private double width;
    private double height;
    private double weight;
    private String nmfc;
    private boolean is_stackable;
    private int quantity;
    private Long pieces;
    private String commodity_type_other;
    private String sku;
}

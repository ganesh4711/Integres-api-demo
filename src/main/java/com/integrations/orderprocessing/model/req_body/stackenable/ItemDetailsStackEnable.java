package com.integrations.orderprocessing.model.req_body.stackenable;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDetailsStackEnable implements Serializable{
    private String productName = "";
    private String action = "";
    private String commodityId = "";
    private String freightClass = "";
    private String description = "";
    private String dimensions = "";
    private double feet;
    private double volume;
    private double quantity;
    private String measurementUnit = "";
    private String weightUnit = "";
    private double weight;
    private String dockLocation = "";
    private String itemStatus = "";
    private String skuId = "";
    private String itemNumber = "";
    private String cartonLabel = "";
    private String palletLabel = "";
    private String valuationType = "";
    private String itemId=""; // Newly Added
}

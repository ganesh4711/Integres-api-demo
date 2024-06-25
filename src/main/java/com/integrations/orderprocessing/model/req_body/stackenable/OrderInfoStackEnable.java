package com.integrations.orderprocessing.model.req_body.stackenable;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderInfoStackEnable implements Serializable{
    private String pickupStartAt="";
    private String pickupEndAt="";
    private double quantity;
    private String weight = "";
    private String orderDate = "";
    private double dimensions;
    private String truckNumber = "";
    private String doorNumber = "";
    private String orderKeyIdentifier = "";
    private int numberOfPallets;
    private String bolUrl = "";
    private String shipmentNumber = "";  // New
    private String deliveryNumber = "";  // New
}

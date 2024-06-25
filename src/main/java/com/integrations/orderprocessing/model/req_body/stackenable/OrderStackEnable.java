package com.integrations.orderprocessing.model.req_body.stackenable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderStackEnable implements Serializable{
    private String orderId = "";
    private String orderNumber = "";
    private String orderType = "";
    private String carrierName = "";
    private String wareHouseName = "";
    private String shipperName = "";
    private AddressStackEnable originAddress;
    private AddressStackEnable destinationAddress;
    private String orderReceivedDate="";
    private String orderDispatchedDate="";
    private String referenceNumber = "";
    private String hawbNumber = "";
    private String mawbNumber = "";
    private String legId = "";
    private String recordStatus = "";
    private String orderStatus = "";
    private String notes = "";
    private String source = "";
    private String organizationId = "";
    private String organizationName = ""; // New
    private String actionCode=""; // New
    private String partnerCarrierName=""; // New
    private List<String> accessorialNames = new ArrayList<String>(); // New
    private String tag="";  // New
    private String proNumber=""; // Newly Added
}

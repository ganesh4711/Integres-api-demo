package com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerDetails {
    private String parcelType="";
}

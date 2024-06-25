package com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm;

import lombok.Data;

@Data
public class ItemLabelDetails {
	private String labelType="";
	private String palletNumber="";
	private String palletLabelId="";
	private String cartonLabelId="";
	private int quantity;
}
package com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm;

import lombok.Data;

@Data
public class CarrierTrkgDlvry {
	private String containerNumber="";
	private String sealNumber="";
	private String proNumber="";
	private String manifestNumber="";
	private String parcelTrackNumber="";
	private String customerLoadId="";
	private int totalNoofWMSShipmentsInTruck;
}

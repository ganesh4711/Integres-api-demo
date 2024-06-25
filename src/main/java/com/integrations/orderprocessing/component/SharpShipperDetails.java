package com.integrations.orderprocessing.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SharpShipperDetails {

	@Value("${shipper.SHARP.org-id}")
	public String organizationId;

	@Value("${shipper.SHARP.source}")
	public String source;

	@Value("${shipper.SHARP.carrier-name}")
	public String carrierName;

	@Value("${shipper.SHARP.warehouse-name}")
	public String wareHouseName;

	@Value("${shipper.SHARP.shipper-name}")
	public String shipperName;
	
	@Value("${shipper.SHARP.inventory-sync.path-param}")
	public String inventorySyncPathParam;

}

package com.integrations.orderprocessing.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BotristaShipperDetails {

	@Value("${shipper.BOTRISTA.org-id}")
	public String organizationId;

	@Value("${shipper.BOTRISTA.source}")
	public String source;

	@Value("${shipper.BOTRISTA.carrier-name}")
	public String carrierName;

	@Value("${shipper.BOTRISTA.warehouse-name}")
	public String wareHouseName;

	@Value("${shipper.BOTRISTA.shipper-name}")
	public String shipperName;
	
	@Value("${shipper.BOTRISTA.inventory-sync.path-param}")
	public String inventorySyncPathParam;

}

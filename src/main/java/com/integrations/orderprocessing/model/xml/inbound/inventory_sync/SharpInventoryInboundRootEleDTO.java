package com.integrations.orderprocessing.model.xml.inbound.inventory_sync;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "ZWMSINV_RECON")
@XmlAccessorType(XmlAccessType.FIELD)
public class SharpInventoryInboundRootEleDTO {
	@XmlElement(name = "IDOC")
	private SharpInventoryInboundData idoc = new SharpInventoryInboundData();
}

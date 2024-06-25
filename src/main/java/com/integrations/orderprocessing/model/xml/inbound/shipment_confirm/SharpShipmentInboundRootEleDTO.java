package com.integrations.orderprocessing.model.xml.inbound.shipment_confirm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "ZSHPMNT05")
@XmlAccessorType(XmlAccessType.FIELD)
public class SharpShipmentInboundRootEleDTO {

	@XmlElement(name = "IDOC")
	private SharpShipmentInboundData idoc = new SharpShipmentInboundData();
}

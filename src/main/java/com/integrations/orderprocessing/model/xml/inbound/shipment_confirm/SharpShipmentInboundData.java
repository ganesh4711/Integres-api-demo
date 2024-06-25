package com.integrations.orderprocessing.model.xml.inbound.shipment_confirm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SharpShipmentInboundData {

	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "EDI_DC40")
	private ShipmentInEDI_DC40 edi_dc40 = new ShipmentInEDI_DC40();
	@XmlElement(name = "E1EDT20")
	private ShipmentInDetails shipmentInDetails = new ShipmentInDetails();
}



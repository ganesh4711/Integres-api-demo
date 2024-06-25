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
public class ShipmentInDetails {

	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "TKNUM")
	private String shipmentNum;
	@XmlElement(name = "EXTI1")
	private String BOLNum;
	@XmlElement(name = "EXTI2")
	private String deliveryNum;
	@XmlElement(name = "E1EDL20")
	private ShipmentInDeliveryDetails deliveryDetails = new ShipmentInDeliveryDetails();
}



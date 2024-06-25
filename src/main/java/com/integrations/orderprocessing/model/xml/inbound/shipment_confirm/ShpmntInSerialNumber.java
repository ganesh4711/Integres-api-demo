package com.integrations.orderprocessing.model.xml.inbound.shipment_confirm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ShpmntInSerialNumber {
	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	
	@XmlElement(name = "SERNR")
	private String serialNum;
	
	@XmlElement(name = "UII")
	private String serialNum2;

	public ShpmntInSerialNumber(String serialNum) {
		this.serialNum = serialNum;
		this.serialNum2 = serialNum;
	}
}

package com.integrations.orderprocessing.model.xml.inbound.gr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SharpGRInboundData {
	
	@XmlAttribute(name = "BEGIN")
	private String begin = "1";

	@XmlElement(name = "EDI_DC40")
	private SharpGREDI_DC40 sharpGREDI_DC40 = new SharpGREDI_DC40();
	@XmlElement(name = "E1MBXYH")
	private SharpGoodsMovementData sharpGoodsMovementData = new SharpGoodsMovementData();

}

package com.integrations.orderprocessing.model.xml.inbound.gr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "WMMBID02")
@XmlAccessorType(XmlAccessType.FIELD)
public class SharpGRInboundRootEleDTO {
	@XmlElement(name = "IDOC")
	private SharpGRInboundData sharpGRInboundData = new SharpGRInboundData();
}

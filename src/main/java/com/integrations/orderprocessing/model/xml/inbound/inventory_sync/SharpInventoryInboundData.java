package com.integrations.orderprocessing.model.xml.inbound.inventory_sync;

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
public class SharpInventoryInboundData {
	@XmlAttribute(name = "BEGIN")
	private String begin = "1";
	@XmlElement(name = "EDI_DC40")
	private SharpInventoryEDI_DC40 edi_dc40 = new SharpInventoryEDI_DC40();
	@XmlElement(name = "Z1KINVRECON")
	private List<ProductDetails> productDetailsList = new ArrayList<ProductDetails>();
}



package com.integrations.orderprocessing.model.xml.inbound.shipment_confirm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class ShpmntInDlvryDeadlineDtls {
	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "QUALF")
	private String qualifier;
	@XmlElement(name = "NTANF")
	private String shippedDlvryDate1;
	@XmlElement(name = "NTANZ")
	private String shippedDlvryDate2;
	@XmlElement(name = "NTEND")
	private String shippedDlvryDate3;
	@XmlElement(name = "IEDD")
	private String iedd;
	@XmlElement(name = "IEDZ")
	private String iedz;
}

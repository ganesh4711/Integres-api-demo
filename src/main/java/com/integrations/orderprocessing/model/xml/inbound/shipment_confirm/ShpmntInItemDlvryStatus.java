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
public class ShpmntInItemDlvryStatus {
	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "QUALF")
	private String qualifier;

	public ShpmntInItemDlvryStatus(String qualifier) {
		this.qualifier = qualifier;
	}
}

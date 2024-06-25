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
public class ShipmentInTrackingDetails {

	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "TRACK_QUAL")
	private String qualifier;
	@XmlElement(name = "TRACK_NO")
	private String trackNo;

	public ShipmentInTrackingDetails(String qualifier) {
		this.qualifier = qualifier;
	}
}
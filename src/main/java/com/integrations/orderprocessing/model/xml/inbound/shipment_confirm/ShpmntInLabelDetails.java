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
public class ShpmntInLabelDetails {
	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "LABEL_TYPE")
	private String labelType;
	@XmlElement(name = "PALLET_NO", nillable = true)
	private String palletNo;
	@XmlElement(name = "PALLET_LABEL_ID", nillable = true)
	private String palletLabelId;
	@XmlElement(name = "CARTON_LABEL_ID")
	private String cartonLabelId;
	@XmlElement(name = "QUANTITY")
	private int quantity;
	@XmlElement(name = "QUANTITY_UOM")
	private String unitOfMeasure="EA";
}

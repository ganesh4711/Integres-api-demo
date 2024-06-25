package com.integrations.orderprocessing.model.xml.inbound.inventory_sync;

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
public class ProductDetails {

	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "MATERIAL")
	private String prodId;
	@XmlElement(name = "VALUATION_TYPE")
	private String valuationType;
	@XmlElement(name = "PLANT")
	private String plantId;
	@XmlElement(name = "STGE_LOC")
	private String storageLocation;
	@XmlElement(name = "QTY")
	private int quantity;
	@XmlElement(name = "UOM")
	private String unitOfMeasure;
	@XmlElement(name = "DOC_DATE")
	private String docDate;
	@XmlElement(name = "PLAN_DATE")
	private String planDate;
	@XmlElement(name = "COUNT_DATE")
	private String countDate;
	@XmlElement(name = "PHYS_INV_REF", nillable = true)
	private String physInvRef;
}

package com.integrations.orderprocessing.model.xml.inbound.gr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SharpGRItem {

	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";

	@XmlElement(name = "MATNR")
	private String prodId = "";
	@XmlElement(name = "WERKS")
	private String plantId = "SD01";
	@XmlElement(name = "LGORT")
	private String strgLoc = "1000";
	@XmlElement(name = "BWART")
	private String bwart = "101";
	@XmlElement(name = "ERFMG")
	private int quantity;
	@XmlElement(name = "ERFME")
	private String unitOfMeasure = "EA";
	@XmlElement(name = "EBELN")
	private String purchOrderNum = "";
	@XmlElement(name = "EBELP")
	private String itemNum = "";
	@XmlElement(name = "KZBEW")
	private String kzbew = "B";
	@XmlElement(name = "BWTAR")
	private String itemValuationType = "";
}

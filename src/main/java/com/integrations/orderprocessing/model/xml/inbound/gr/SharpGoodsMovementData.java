package com.integrations.orderprocessing.model.xml.inbound.gr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SharpGoodsMovementData {
	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "BLDAT")
	private String goodsRcvdDate = "";
	@XmlElement(name = "BUDAT")
	private String goodsRcvdDate2 = "";
	@XmlElement(name = "XBLNR")
	private String refNum = "";
	@XmlElement(name = "BKTXT")
	private String extrnlSysId = "";
	@XmlElement(name = "TCODE")
	private String txnCode = "MIGO";
	@XmlElement(name = "E1MBXYI")
	private SharpGRItem sharpGRItem = new SharpGRItem();
}

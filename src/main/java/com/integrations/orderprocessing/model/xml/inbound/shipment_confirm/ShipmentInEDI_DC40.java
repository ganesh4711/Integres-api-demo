package com.integrations.orderprocessing.model.xml.inbound.shipment_confirm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ShipmentInEDI_DC40 {

	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "TABNAM")
	private String tabnam = "EDI_DC40";
	@XmlElement(name = "MANDT")
	private String mandt = "110";
	@XmlElement(name = "DOCNUM")
	private String docnum = "20242305112351817";
	@XmlElement(name = "DOCREL")
	private String docrel = "640";
	@XmlElement(name = "DIRECT")
	private String direct = "2";
	@XmlElement(name = "IDOCTYP")
	private String idoctyp = "ZSHPMNT05";
	@XmlElement(name = "MESTYP")
	private String mestyp = "SHPMNT";
	@XmlElement(name = "SNDPRT")
	private String sndprt = "LS";
	@XmlElement(name = "SNDPRN")
	private String sndprn = "WMS_SD1";
	@XmlElement(name = "RCVPRT")
	private String rcvprt = "LS";
	@XmlElement(name = "RCVPRN")
	private String rcvprn = "SAP";
}

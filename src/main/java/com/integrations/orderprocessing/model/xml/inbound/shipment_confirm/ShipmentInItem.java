package com.integrations.orderprocessing.model.xml.inbound.shipment_confirm;

import java.util.ArrayList;
import java.util.List;

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
public class ShipmentInItem {
	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "POSNR")
	private String itemNum;
	@XmlElement(name = "MATNR")
	private String prodNum;
	@XmlElement(name = "WERKS")
	private String plantId = "SD01";
	@XmlElement(name = "LGORT")
	private String storageLocation = "1000";
	@XmlElement(name = "LFIMG")
	private int quantity;
	@XmlElement(name = "VRKME")
	private String unitOfMeasure = "EA";
	@XmlElement(name = "E1EDL19")
	private ShpmntInItemDlvryStatus itemdlvryStatus = new ShpmntInItemDlvryStatus("BAS");
	@XmlElement(name = "Z1PLABEL")
	private List<ShpmntInLabelDetails> labelDtlsList= new ArrayList<ShpmntInLabelDetails>();
	@XmlElement(name = "E1EDL11")
	private List<ShpmntInSerialNumber> serialNumList = new ArrayList<ShpmntInSerialNumber>();
}

package com.integrations.orderprocessing.model.xml.inbound.shipment_confirm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ShipmentInDeliveryDetails {

	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "VBELN")
	private String deliveryNum;
	@XmlElement(name = "BTGEW")
	private float totalWeight = 0.0000f;
	@XmlElement(name = "ANZPK")
	private int totalNoofPkgsInDlvry;
	@XmlElement(name = "BOLNR")
	private String BOLNum;
	@XmlElement(name = "TRAID", nillable = true)
	private String traid;
	@XmlElement(name = "LIFEX", nillable = true)
	private String lifex;
	@XmlElement(name = "ROUTE", nillable = true)
	private String route;
	
	@XmlElement(name = "Z1KTRACKING")
	private List<ShipmentInTrackingDetails> trackingDtlsList = new ArrayList<ShipmentInTrackingDetails>();
	@XmlElement(name = "E1EDL18")
	private List<DeliveryController> deliveryControllerList = new ArrayList<DeliveryController>(
			Arrays.asList(new DeliveryController[] { new DeliveryController("PGI"), new DeliveryController("PIC") }));
	@XmlElement(name = "E1ADRM1")
	private ShipmentInPartnerDetails partnerDetails = new ShipmentInPartnerDetails();
	@XmlElement(name = "E1EDT13")
	private ShpmntInDlvryDeadlineDtls dlvryDeadlineDtls = new ShpmntInDlvryDeadlineDtls();
	@XmlElement(name = "E1EDL24")
	private List<ShipmentInItem> itemList = new ArrayList<ShipmentInItem>();
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
class DeliveryController {

	@XmlAttribute(name = "SEGMENT")
	private String segment = "1";
	@XmlElement(name = "QUALF")
	private String qualif;
	
	public DeliveryController(String qualif) {
		this.qualif = qualif;
	}
}



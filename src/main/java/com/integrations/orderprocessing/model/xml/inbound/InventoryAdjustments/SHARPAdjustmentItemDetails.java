package com.integrations.orderprocessing.model.xml.inbound.InventoryAdjustments;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SHARPAdjustmentItemDetails {

    @XmlAttribute(name = "SEGMENT")
    private String segment= "1";
    @XmlElement(name = "MATNR")
    private String productId;
    @XmlElement(name = "WERKS")
    private String plantId;
    @XmlElement(name = "LGORT")
    private String storageLocation;
    @XmlElement(name = "BWART")
    private String movementType;
    @XmlElement(name = "BWTAR")
    private String valuationType="";
    @XmlElement(name = "ERFMG")
    private String quantity;
    @XmlElement(name = "ERFME")
    private String unitEntry = "EA";
    @XmlElement(name = "UMMAT")
    private String receivingProductId;
    @XmlElement(name = "UMWRK")
    private String receivingPlantId;
    @XmlElement(name = "UMLGO")
    private String receivingLocation;
    @XmlElement(name = "UMBAR")
    private String receivedItemValuationType;

}
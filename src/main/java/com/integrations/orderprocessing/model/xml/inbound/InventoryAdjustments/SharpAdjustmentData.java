package com.integrations.orderprocessing.model.xml.inbound.InventoryAdjustments;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SharpAdjustmentData {

    @XmlAttribute(name = "SEGMENT")
    private String SEGMENT = "1";
    @XmlElement(name = "BLDAT")
    private String goodsMovementDate1;
    @XmlElement(name = "BUDAT")
    private String goodsMovementDate2;
    @XmlElement(name = "XBLNR")            //truck number
    private String referenceNumber;
    @XmlElement(name = "BKTXT")            //  doc header text  (for TCODE 309 it is blank)
    private String  note = "";
    @XmlElement(name = "XABLN")
    private String adjustmentNumber;
    @XmlElement(name = "TCODE")
    private String transactionCode;
    @XmlElement(name = "E1MBXYI")
    private SHARPAdjustmentItemDetails adjustmentItemDetails = new SHARPAdjustmentItemDetails();
}

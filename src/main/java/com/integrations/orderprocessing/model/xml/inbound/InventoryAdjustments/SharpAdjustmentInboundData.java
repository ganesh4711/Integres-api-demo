package com.integrations.orderprocessing.model.xml.inbound.InventoryAdjustments;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SharpAdjustmentInboundData {

    @XmlAttribute(name = "BEGIN")
    private String begin = "1";

    @XmlElement(name ="EDI_DC40")
    private AdjustmentEDI_DC40 edi_dc40 = new AdjustmentEDI_DC40();

    @XmlElement(name ="E1MBXYH")
    private SharpAdjustmentData adjustmentData = new SharpAdjustmentData();
}


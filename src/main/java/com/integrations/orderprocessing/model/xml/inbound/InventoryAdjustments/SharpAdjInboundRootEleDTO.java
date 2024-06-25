package com.integrations.orderprocessing.model.xml.inbound.InventoryAdjustments;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "WMMBID02")
@XmlAccessorType(XmlAccessType.FIELD)
public class SharpAdjInboundRootEleDTO {

    @XmlElement(name = "IDOC")
    private SharpAdjustmentInboundData idoc = new SharpAdjustmentInboundData();
}


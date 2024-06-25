package com.integrations.orderprocessing.model.xml.inbound.InventoryAdjustments;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class AdjustmentEDI_DC40 {
    @XmlAttribute(name = "SEGMENT")
    private String segment = "1";

    @XmlElement(name = "TABNAM")
    private String tabnam = "EDI_DC40";

    @XmlElement(name = "MANDT")
    private String mandt = "100";

    @XmlElement(name = "DOCNUM")
    private String docnum;                       // timestamp by wms

    @XmlElement(name = "DOCREL")
    private String docrel = "640";

    @XmlElement(name = "DIRECT")
    private String direct = "2";

    @XmlElement(name = "IDOCTYP")
    private String idoctyp  ="WMMBID02";

    @XmlElement(name = "MESTYP")
    private String mestyp ="WMMBXY";

    @XmlElement(name = "SNDPRT")
    private String sndprt = "LS";

    @XmlElement(name = "SNDPRN")
    private String sndprn = "WMS_SD1";

    @XmlElement(name = "RCVPRT")
    private String rcvprt = "LS";

    @XmlElement(name = "RCVPRN")
    private String rcvprn = "SAP";

}

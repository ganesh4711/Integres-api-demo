package com.integrations.orderprocessing.model.req_body.inbound.gr;

import lombok.Data;

@Data
public class GR_GoodsActvtsIssueToDlvry {
    private String qualifier;
    private String shippedDlvryDate;
}

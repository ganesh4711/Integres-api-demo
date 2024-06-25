package com.integrations.orderprocessing.model.req_body.roserocket;

import lombok.Data;

@Data
public class RRManifestCarrierResponse {
    private String partner_carrier_id;
    private String partner_carrier_service_id;
}

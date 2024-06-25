package com.integrations.orderprocessing.model.req_body.roserocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RRCustomer {
    private String id;
    private String external_id;
    private String short_code;
}

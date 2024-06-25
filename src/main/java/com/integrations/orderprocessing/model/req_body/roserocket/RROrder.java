package com.integrations.orderprocessing.model.req_body.roserocket;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RROrder {
    private ZonedDateTime created_at;
    private ZonedDateTime updated_at;
    private String id;
    private String sequence_id;
    private String external_id;
    private String public_id;
    private String tender_id;
    private RRCustomer customer;
    private RRAddress origin;
    private RRAddress destination;
    private RRAddress billing;
    private String status;
    private String billing_option;
    private String notes;
    private String po_num;
    private String tender_num;
    private String ref_num;
    private String custom_broker;
    private String port_of_entry;
    private int declared_value;
    private String declared_value_currency;
    private ZonedDateTime pickup_start_at;
    private String pickup_start_at_local;
    private ZonedDateTime pickup_end_at;
    private String pickup_end_at_local;
    private ZonedDateTime pickup_appt_start_at;
    private String pickup_appt_start_at_local;
    private ZonedDateTime pickup_appt_end_at;
    private String pickup_appt_end_at_local;
    private ZonedDateTime pickup_notes;
    private ZonedDateTime delivery_start_at;
    private String delivery_start_at_local;
    private ZonedDateTime delivery_end_at;
    private String delivery_end_at_local;
    private ZonedDateTime delivery_appt_start_at;
    private String delivery_appt_start_at_local;
    private ZonedDateTime delivery_appt_end_at;
    private String delivery_appt_end_at_local;
    private String delivery_notes;
    private String current_leg_id;
    private ZonedDateTime  pickedup_at;
    private String pickedup_at_local;
    private ZonedDateTime delivered_at;
    private String delivered_at_local;
    private String dim_type;
    private String default_measurement_unit_id;
    private String default_weight_unit_id;
    private List<RRCommodity> commodities;
    private List<String> accessorials;
    private String type;
    private boolean is_multistop_order;
    private Long multistop_order_id;
    private Long multistop_order_full_id;
    private Long multistop_order_sequence_id;
    private String billable_miles;
    private List<String>  auto_post_load_board_connector_ids;
    private String source;
    private String transportation_authority_id;
}

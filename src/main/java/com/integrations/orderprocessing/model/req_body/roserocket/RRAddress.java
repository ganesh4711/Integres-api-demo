package com.integrations.orderprocessing.model.req_body.roserocket;

import java.time.ZonedDateTime;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RRAddress {
	private String address_book_id;
    private String address_book_external_id;
    private String org_name;
    private String contact_name;
    private String address_1;
    private String address_2;
    private String suite;
    private String city;
    private String state;
    private String country;
    private String postal;
    private String phone;
    private String phone_ext;
    private String email;
    private String fax;
    private Double latitude;
    private Double longitude;
    private ZonedDateTime bus_hours_start_at;
    private ZonedDateTime  bus_hours_end_at;
    private TimeZone timezone;
}

package com.integrations.orderprocessing.model.req_body.fleetenable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEStopSave {
    private int stop_num;
    private String reason;
    private String reason_desc;
    private double weight;
    private double volume;
    private int num_items;
    private String uom;
    private String description;
    private String requested_date;
    private String date1_time_zone;
    private String latest_requested_date;
    private String date2_time_zone;
    private int pallets;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String level_of_service;
    private int service_duration;
    private String state;
    private String zip;
    private String country;
    private String type;
    private String code;
    private FEContactSave contact;
    private String note;
    private List<FEReferenceSave> references;
    private List<FEItemSave> items;
    private FEBillToSave bill_to; // Optional
}

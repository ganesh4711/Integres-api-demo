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
public class FEOrderSave {
    private int transaction_id;
    private String customer_identifier;
    private String shipper_code;
    private String carrier_code;
    private String purpose_code;
    private String purpose_description;
    private String shipment_id;
    private String special_handling_code;
    private String special_services_code;
    private String special_handling_desc;
    private String equipment;
    private String equipment_number;
    private int total_pieces_of_equipment;
    private String equip_length;
    private int pallets;
    private double weight;
    private double length;
    private double width;
    private double height;
    private String weight_unit;
    private double volume;
    private String payment_method;
    private boolean is_hazardous;
    private String instructions;
    private String note;
    private List<FEOrderNoteSave> order_notes;
    private List<FEReferenceSave> references;
    private List<FEStopSave> stops;
}














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
public class FEBillToSave {
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String type;
    private String code;
    private FEContactSave contact;
    private String note;
    private List<FEReferenceSave> references;
}

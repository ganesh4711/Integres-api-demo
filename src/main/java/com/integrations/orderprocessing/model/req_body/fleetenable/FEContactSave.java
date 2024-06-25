package com.integrations.orderprocessing.model.req_body.fleetenable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEContactSave {
    private String name1;
    private String phone1;
    private String email1;
    private String name2;
    private String phone2;
    private String email2;
}

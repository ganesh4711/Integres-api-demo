package com.integrations.orderprocessing.model.req_body.stackenable;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressStackEnable implements Serializable {
    private String addressBookId = "";
    private String addressBookExternalId = "";
    private String orgName = "";
    private String contactName = "";
    private String address1 = "";
    private String address2 = "";
    private String suite = "";
    private String city = "";
    private String state = "";
    private String country = "";
    private String postal = "";
    private String phone = "";
    private String phoneExt = "";
    private String email = "";
    private String fax = "";
    private double latitude;
    private double longitude;
    private String requested_date;
}

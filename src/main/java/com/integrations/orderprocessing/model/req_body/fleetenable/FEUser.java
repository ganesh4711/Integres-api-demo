package com.integrations.orderprocessing.model.req_body.fleetenable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FEUser {
	private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String contactNumber;
    private int organizationId;
    private String organizationType;
    private String roleName;
    private String profile_image;
}

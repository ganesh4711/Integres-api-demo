package com.integrations.orderprocessing.secondary_ds.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.integrations.orderprocessing.stackenable.enums.OrganizationType;
import com.integrations.orderprocessing.stackenable.enums.Status;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "organizations")
@Entity
public class Organization extends BaseEntity{

    @Column(name="name")
    private String name;

    @Column(name="code" )
    private String code;
   
    @Column(name="email")
    private String email;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name="website")
    private String website;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="organization_type")
    private OrganizationType organizationType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="status")
    private Status organizationStatus;

    @Column(name="org_unique_code")
    private String orgUniqueCode;

    @Column(name="is_customer")
    private boolean isCustomer;

}

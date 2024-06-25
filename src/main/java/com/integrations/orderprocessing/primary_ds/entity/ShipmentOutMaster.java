package com.integrations.orderprocessing.primary_ds.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "shipment_out_master", uniqueConstraints = {@UniqueConstraint(columnNames = {"shipment_number", "delivery_number", "reference_number"})})
public class ShipmentOutMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_rec_id")
    private Long id;
    
    @Column(name = "created_date")
    private String created_date;

    @Column(name = "created_time")
    private String created_time;
    
    @Column(name = "flow_type")
    private String flowType;

    @Column(name = "shipment_number")
    private String shipmentNumber;

    @Column(name = "delivery_number")
    private String deliveryNumber;

    @Column(name = "reference_number")
    private String referenceNumber;


    @Column(name = "reference_number2")
    private String referenceNumber2;


    @Column(name = "source_partner")
    private String sourcePartner;

    @Column(name = "destination_partner")
    private String destinationPartner;

    @OneToMany(mappedBy = "shipmentOutMaster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShipmentOutItemMaster> shipmentOutItemMasterList;
    
    @Column(name = "status")
    private String status;

    @Column(name = "created_rec_at")
    private Date createdRecAt;

    @PrePersist
    public void initCreatedRecId(){
        this.createdRecAt = new Date();
    }
}
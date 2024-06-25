package com.integrations.orderprocessing.primary_ds.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name="purchase_order_master", schema = "public")
public class PurchaseOrderMaster {

    @Id
    @Column(name = "order_rec_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "created_date")
    private String created_date;

    @Column(name = "created_time")
    private String created_time;

    @Column(name = "flow_type")
    private String flowType;

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "source_partner")
    private String sourcePartner;

    @Column(name = "destination_partner")
    private String destinationPartner;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemMaster> orderItems;

    @Column(name = "status")
    private String status;

    @Column(name = "updated_date")
    private String updatedDate;

    @Column(name = "updated_time")
    private String updatedTime;
}

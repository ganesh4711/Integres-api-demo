package com.integrations.orderprocessing.primary_ds.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data@AllArgsConstructor
@NoArgsConstructor
@Table(name = "shipment_out_item_master")
public class ShipmentOutItemMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_rec_id")
    private Long id;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "quantity")
    private float quantity;

    @Column(name = "warehouse_id")
	private String warehouseId;

    @Column(name = "warehouse_name")
    private String warehouseName;

    @Column(name = "storage_location")
    private String storageLocation;

    @Column(name = "valuation_type")
    private String valuationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "so_rec_id")
    private ShipmentOutMaster shipmentOutMaster;

    @Column(name = "created_date")
    private String created_date;

    @Column(name = "created_time")
    private String created_time;
}
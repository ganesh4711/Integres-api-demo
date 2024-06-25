package com.integrations.orderprocessing.primary_ds.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "goods_receipt_log", schema = "public")
public class GoodsReceiptLog {

    @Id
    @Column(name = "gr_rec_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "gr_date")
    private String goodsReceivedDate;

    @Column(name = "trailer_number")
    private String trailerNumber;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "plant_id")
    private String plantId;

    @Column(name = "storage_location")
    private String storageLocation;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "valuation_type")
    private String valuationType;

    @Column(name = "flow_type")
    private String flowType;

    @Column(name = "status")
    private boolean status;

    @Column(name = "created_at")
    private Date createdAt;
    
    @Column(name = "file_name")
    private String fileName;
}

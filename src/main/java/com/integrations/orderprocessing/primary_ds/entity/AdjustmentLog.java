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
@Table(name = "adjustment_log", schema = "public")
public class AdjustmentLog {
    @Id
    @Column(name = "adj_rec_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note")
    private String note;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "movement_type")
    private String movementType;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "from_product_id")
    private String fromProductId;

    @Column(name = "from_plant_id")
    private String fromPlantId;

    @Column(name = "from_storage_location")
    private String fromStorageLocation;

    @Column(name = "from_item_valuation")
    private String fromItemValuationType;

    @Column(name = "to_product_id")
    private String toProductId;

    @Column(name = "to_plant_id")
    private String toPlantId;

    @Column(name = "to_storage_location")
    private String toStorageLocation;

    @Column(name = "to_item_valuation")
    private String toItemValuationType;

    @Column(name = "created_rec_at")
    private Date createdRecAt;

    @PrePersist
    public void initCreatedRecId(){
        this.createdRecAt = new Date();
    }
}

package com.integrations.orderprocessing.secondary_ds.entity;

import com.integrations.orderprocessing.stackenable.enums.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "product")
@Entity
public class Product extends BaseEntity {
    @Column(name = "product_name")
    @NotBlank(message = "product name not be blank")
    private String productName;

    @Column(name = "version_number")
    private String versionNumber;

    @ManyToOne
    @JoinColumn(name = "shipper_id")
    private Organization shipperId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "category not be blank")
    private ProductCategory category;

    @Column(name = "length")
    private double length;

    @Column(name = "breadth")
    private double breadth;

    @Column(name = "height")
    private double height;

    @Column(name = "weight")
    private double weight;

    @Column(name = "upc")
    private  String upc;

    @Column(name = "threshold")
    private int threshold;

    @Column(name = "upload_image")
    private String uploadImage;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @Column(name = "is_serializable")
    private boolean isSerializable;

    @Column(name = "container_type")
    private String containerType;

    @Column(name ="item_catg_group")
    private String itemCategoryGroup;
    
    @Column(name ="small_parcel")
    private String smallParcel;
}

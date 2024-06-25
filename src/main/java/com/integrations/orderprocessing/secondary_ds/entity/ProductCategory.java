package com.integrations.orderprocessing.secondary_ds.entity;

import com.integrations.orderprocessing.stackenable.enums.Status;

import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "product_category")
@Entity
public class ProductCategory extends BaseEntity{
    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name="shipper_id" )
    private Organization shipperOrganization;

//    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
//    private List<Product> productList;

}

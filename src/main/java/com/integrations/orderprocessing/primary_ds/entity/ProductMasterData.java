package com.integrations.orderprocessing.primary_ds.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_master_data", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMasterData {

	@Id
	@Column(name = "product_id")
	private String prodId;

	@Column(name = "product_description")
	private String productDescription;

	@Column(name = "estim_gross_weight_lb")
	private Double estimGrossWeightLb;

	@Column(name = "ean")
	private Long ean;

	@Column(name = "country_of_origin")
	private String countryOfOrigin;

	@Column(name = "sharp_product_department")
	private Integer sharpProductDepartment;

	@Column(name = "sharp_product_line")
	private String sharpProductLine;

	@Column(name = "length_in")
	private Double lengthIn;

	@Column(name = "width_in")
	private Double widthIn;

	@Column(name = "height_in")
	private Double heightIn;

	@Column(name = "is_serializable")
	private String isSerializable;
	@Column(name = "small_parcel")
	private String smallParcel;

	@Column(name = "item_catg_group")
	private String itemCatgGroup;

	@Column(name = "comm_imp_code_n")
	private Double commImpCodeN;

	@Column(name = "export_control")
	private String exportControl;

	@Column(name = "origin")
	private String origin;
}
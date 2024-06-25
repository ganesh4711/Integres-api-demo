package com.integrations.orderprocessing.primary_ds.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "purchase_order_item_log", schema = "public")
public class OrderItemLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="item_rec_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_rec_id")
	private PurchaseOrderLog purchaseOrderLog;

	@Column(name = "product_id")
	private String productId;

	@Column(name = "created_date")
	private String created_date;

	@Column(name = "created_time")
	private String created_time;

	@Column(name = "item_number")
	private String itemNumber;

	@Column(name = "action")
	private String action;

	@Column(name = "quantity")
	private float quantity;

	@Column(name = "warehouse_id")
	private String warehouseId;

	@Column(name = "valuation_type")
	private String valuationType;

	@Column(name = "status")
	private String status;
}
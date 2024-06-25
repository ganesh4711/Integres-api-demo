package com.integrations.orderprocessing.primary_ds.entity;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shipment_confirm_log", schema = "public")
public class ShipmentConfirmLog {
	@Id
	@Column(name = "sc_rec_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "shipment_number")
	private String shipmentNumber;
	
	@Column(name = "delivery_number")
	private String deliveryNumber;
	
	@Column(name = "flow_type")
	private String flowType;
	
	@Column(name = "status")
	private boolean status;
	
	@Column(name = "file_name")
    private String fileName;
}

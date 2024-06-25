package com.integrations.orderprocessing.primary_ds.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "rr_manifest_assign", schema = "public",  uniqueConstraints = {@UniqueConstraint(columnNames = {"manifest_id"})})
public class RRManifestAssignEventEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "event")
	private String event;

	@Column(name = "manifest_id")
	private String manifestId;
	
	@Column(name = "order_id")
	private String orderId;

	@Column(name = "time_stamp")
	private String timestamp;
	
	@Column(name = "source")
	private String source;
	
	@Column(name = "customer_id")
	private String customerId;

	public RRManifestAssignEventEntity(String event, String manifestId, String orderId, String timestamp) {
		this.event = event;
		this.manifestId = manifestId;
		this.orderId = orderId;
		this.timestamp = timestamp;
	}
}

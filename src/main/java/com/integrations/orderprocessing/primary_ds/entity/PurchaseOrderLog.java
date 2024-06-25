package com.integrations.orderprocessing.primary_ds.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "purchase_order_log", schema = "public")
public class PurchaseOrderLog {

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

	@OneToMany(mappedBy = "purchaseOrderLog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderItemLog> orderItemsLog;

	@Column(name = "status")
	private String status;
	
	@Column(name = "created_rec_at")
    private Date createdRecAt;

    @PrePersist
    public void initCreatedRecId(){
        this.createdRecAt = new Date();
    }
}

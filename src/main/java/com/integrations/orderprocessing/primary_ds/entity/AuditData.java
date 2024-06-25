package com.integrations.orderprocessing.primary_ds.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "AUDIT_DATA", schema = "public")
public class AuditData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "PROCESS_DATE")
	private Date processDate;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "CHANNEL")
	private String channel;

	@Column(name = "IP")
	private String ip;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "PROCESS_REF_NUM")
	private String processRefNum;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "FLOW_TYPE")
	private String flowType;
}

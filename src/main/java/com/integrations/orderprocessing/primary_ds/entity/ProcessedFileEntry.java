package com.integrations.orderprocessing.primary_ds.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "PROCESSED_FILE_ENTRY", schema = "public")
public class ProcessedFileEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "PROCESSED_DATE")
	private Date processedDate;

	@Column(name = "STATUS")
	private boolean status;

	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "flow_type")
	private String flowType;
}

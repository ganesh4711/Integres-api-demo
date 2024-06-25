package com.integrations.orderprocessing.secondary_ds.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long Id;

	@Column(name = "created_userid")
	private Long createdUserId;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_userid")
	private Long updatedUserId;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}

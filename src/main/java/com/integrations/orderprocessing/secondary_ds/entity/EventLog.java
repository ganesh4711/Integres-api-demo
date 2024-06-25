package com.integrations.orderprocessing.secondary_ds.entity;

import com.integrations.orderprocessing.stackenable.enums.EventStatus;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "event_log")
@Entity
public class EventLog extends BaseEntity{

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "manifest_id")
    private String manifestId;

    @Column(name = "event_status")
    private String eventStatus; // Use this

    @Column(name = "timestamp")
    private String timestamp;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="status")
    private EventStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "customer_id")
    private String customerId;
}

package com.integrations.orderprocessing.secondary_ds.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.integrations.orderprocessing.primary_ds.entity.RRManifestAssignEventEntity;
import com.integrations.orderprocessing.secondary_ds.entity.EventLog;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {

    @Query(value = "SELECT * FROM event_log el WHERE el.order_id=:orderId and el.event_name=:eventName ORDER BY el.created_at DESC LIMIT 1", nativeQuery = true)
    Optional<EventLog> findLastByOrderId(@Param("orderId")String orderId, @Param("eventName") String eventName);
    
    @Query(value = "SELECT * FROM event_log WHERE manifest_id=:manifestId and event_name=:eventName LIMIT 1", nativeQuery = true)
	Optional<EventLog> getEventLogByManifestIdAndName(@Param("manifestId") String manifestId, @Param("eventName") String eventName);
    
    @Query(value = "SELECT count(*) FROM event_log el WHERE el.order_id=:orderId and el.event_name=:eventName", nativeQuery = true)
	int getManifestAssignCountByOrderIdAndEventName(@Param("orderId")String orderId, @Param("eventName") String eventName);
}

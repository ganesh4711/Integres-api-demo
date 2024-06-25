package com.integrations.orderprocessing.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.component.BotristaShipperDetails;
import com.integrations.orderprocessing.component.SharpShipperDetails;
import com.integrations.orderprocessing.constants.StringConstants;
import com.integrations.orderprocessing.model.req_body.roserocket.RRManifestAssignEvent;
import com.integrations.orderprocessing.secondary_ds.entity.EventLog;
import com.integrations.orderprocessing.secondary_ds.repository.EventLogRepository;
import com.integrations.orderprocessing.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RRManifestAssignEventService {
	
	@Autowired
	RoseRocketService rrService;
	
	//@Autowired
	//RRManifestAssignEventRepository rrManifestAssignEventRepo;
	
	@Autowired
	EventLogRepository  eventLogRepository;

	@Autowired
	SharpShipperDetails sharpShipperDetails;
	
	@Autowired
	BotristaShipperDetails botristaShipperDetails;
	
	public EventLog onSaveRRManifestAssignEvent(String customerType, RRManifestAssignEvent rrObj, String orderId){
		
		EventLog mEventLog = null;
		
		Optional<EventLog> existedManifestAssigned = eventLogRepository.findLastByOrderId(orderId, rrObj.getEvent());
		
		String timestamp = DateUtil.getCrntDTinReqFormat(StringConstants.DATE_TIME_PATTERN6).split("\\+")[0]+"Z";
		if (!existedManifestAssigned.isPresent()) {
			mEventLog = new EventLog();
			mEventLog.setEventName(rrObj.getEvent());
			mEventLog.setOrderId(orderId);
			mEventLog.setEventStatus("NEW");
			mEventLog.setManifestId(rrObj.getManifest_id());
			mEventLog.setTimestamp(timestamp);
			mEventLog.setCreatedUserId(1L); // Check this
			mEventLog.setCreatedAt(DateUtil.getCrntTimeStampinReqFormat(StringConstants.DATE_TIME_PATTERN7));

			String customerId = rrService.getCustomerIdByCustomerType(customerType);
			mEventLog.setCustomerId(customerId);
		}else {
			EventLog existedEventLog = existedManifestAssigned.get();
			
			mEventLog = new EventLog();
			mEventLog.setEventName(existedEventLog.getEventName());
			mEventLog.setOrderId(existedEventLog.getOrderId());
			mEventLog.setEventStatus("UPDATE");
			mEventLog.setManifestId(rrObj.getManifest_id());
			mEventLog.setTimestamp(timestamp);
			mEventLog.setCreatedUserId(existedEventLog.getCreatedUserId());
			mEventLog.setCreatedAt(existedEventLog.getCreatedAt());
			mEventLog.setUpdatedAt(DateUtil.getCrntTimeStampinReqFormat(StringConstants.DATE_TIME_PATTERN7));
			mEventLog.setCustomerId(existedEventLog.getCustomerId());
		}
		
		return eventLogRepository.save(mEventLog);
	}
	
	public boolean isManifestIdExisted(String manifestId, String eventName) {
		Optional<EventLog> existedManifestAssignEvent = eventLogRepository.getEventLogByManifestIdAndName(manifestId, eventName);
		
		return existedManifestAssignEvent.isPresent();
	}
	
	public Optional<String> getManifestAssignCountBySource(String orderId, String source){
		String retValue= null;
		
        try {
        	int count = eventLogRepository.getManifestAssignCountByOrderIdAndEventName(orderId, "manifest.assigned");
			retValue = (count > 1)? "002":"001";
		} catch (Exception e) {
			log.info("Exception in getting Action code by Manifest Assign Count in DB");
		}
        
		return Optional.of(retValue);
	}
}

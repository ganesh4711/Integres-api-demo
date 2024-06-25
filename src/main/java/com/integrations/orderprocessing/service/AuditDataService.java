package com.integrations.orderprocessing.service;


import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.primary_ds.entity.AuditData;
import com.integrations.orderprocessing.primary_ds.repository.AuditDataRepository;
import com.integrations.orderprocessing.util.SystemUtil;

@Service
public class AuditDataService {
	
	@Autowired
	AuditDataRepository auditDataRepository;
	
	public void insertAuditData(String msg, String refNum, String status, String flowType) {
		AuditData obj = new AuditData();
		obj.setChannel("PARSER");
		obj.setFlowType(flowType);
		//obj.setIp(SystemUtil.getSystemIP());
		obj.setMessage(msg);
		obj.setProcessDate(new Date(System.currentTimeMillis()));
		obj.setProcessRefNum(refNum);
		obj.setStatus(status); // "Success"
		obj.setUserId("SELF"); // 
		
		auditDataRepository.save(obj);
	}

}

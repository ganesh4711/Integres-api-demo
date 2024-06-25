package com.integrations.orderprocessing.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.primary_ds.entity.ProcessedFileEntry;
import com.integrations.orderprocessing.primary_ds.repository.ProcessedFileEntryRepository;

@Service
public class ProcessedFileEntryService {
	
	@Autowired
	ProcessedFileEntryRepository mProcessedFileEntryRepository;
	
    public ProcessedFileEntry getEntryByFileName(String fileName) {
    	return mProcessedFileEntryRepository.getEntryByFileName(fileName);
    }
    
    public void saveFileName(String fileName, String remarks, boolean status, String flowType) {
    	ProcessedFileEntry obj = new ProcessedFileEntry();
    	obj.setFileName(fileName);
    	obj.setRemarks(remarks);
    	obj.setStatus(status);
    	obj.setFlowType(flowType);
    	obj.setProcessedDate(new Date(System.currentTimeMillis()));
    	
    	mProcessedFileEntryRepository.save(obj);
    }
}

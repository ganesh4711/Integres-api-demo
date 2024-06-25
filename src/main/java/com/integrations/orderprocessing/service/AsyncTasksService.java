package com.integrations.orderprocessing.service;

import static com.integrations.orderprocessing.constants.FileConstants.*;
import static com.integrations.orderprocessing.constants.StringConstants.DATE_TIME_PATTERN;
import static com.integrations.orderprocessing.constants.StringConstants.FAILED;
import static com.integrations.orderprocessing.constants.StringConstants.OUTWARD;
import static com.integrations.orderprocessing.constants.StringConstants.SUCCESS;
import static com.integrations.orderprocessing.util.DateUtil.getCrntDTTMinReqFormat;
import static com.integrations.orderprocessing.util.XmlDocumentUtil.removeXmlAttributes;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.component.Utils;
import com.integrations.orderprocessing.model.xml.inbound.InventoryAdjustments.SharpAdjInboundRootEleDTO;
import com.integrations.orderprocessing.model.xml.inbound.gr.SharpGRInboundRootEleDTO;
import com.integrations.orderprocessing.model.xml.inbound.shipment_confirm.SharpShipmentInboundRootEleDTO;
import com.integrations.orderprocessing.util.SFTPConnectionUtil;
import com.jcraft.jsch.ChannelSftp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AsyncTasksService {
	
	@Autowired
	ProcessedFileEntryService mProcessedFileEntryService;
	
	@Autowired
	GoodsReceiptLogService mGoodsReceiptLogService;
	
	@Autowired
	ShipmentConfirmLogService mShipmentConfirmLogService;
	
	@Autowired
	AuditDataService mAuditDataService;

	@Autowired
	Utils utils;

	@Value("${app.processed.file.save-in-db}")
	private String isFileNameSaveInDB;
	
	@Async
	public CompletableFuture<String> onWriteXMLFileToSFTP(SFTPConnectionUtil sftpConnectionUtil, List<Map<Long, Object>> sendObjList, String flowRefNum) {
			log.info("File(s) being saved(onWriteXMLFileToSFTP)...");
			
			try {
				 Set<Map.Entry<Long, Object>> entrySet = sendObjList
			 	.stream()
			 	.flatMap(map -> map.entrySet().stream())
			 	.collect(Collectors.toSet());
				 
				for(Map.Entry<Long, Object> entry : entrySet) {
			 		Long recId = entry.getKey();
			 		Object obj = entry.getValue();
			 		
					callWriteXMLFileModule(obj, sftpConnectionUtil.getChannelSftp(), flowRefNum, recId);
				}
			} catch (Exception e) {
				String remarks = "Exception in saving xml file to STFP(onWriteXMLFileToSFTP):: "+e.getMessage();
				log.info(remarks);
				mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, OUTWARD);
			}
	 		finally {
				log.info("Status Before Closing Session(onWriteXMLFileToSFTP) :: "
						+ sftpConnectionUtil.getSession().isConnected());
				sftpConnectionUtil.getChannelSftp().exit();
				sftpConnectionUtil.getChannelSftp().disconnect();

				if (sftpConnectionUtil.getSession().isConnected()) {
					sftpConnectionUtil.getSession().disconnect();
				}
				log.info("Status After Closing Session(onWriteXMLFileToSFTP) :: "
						+ sftpConnectionUtil.getSession().isConnected());
			}
			
			log.info("File(s) saved done(onWriteXMLFileToSFTP).");
			return CompletableFuture.completedFuture("Done");
	}
	
	
	
	private synchronized void callWriteXMLFileModule(Object sendObj, ChannelSftp sftpChannel, String flowRefNum, Long recId) throws Exception {
		String fileName = writeXMLFile(sendObj, sftpChannel, flowRefNum);
		log.info("RecId:: {} and FileName:: {}", recId, fileName );
		onUpdateFileNameInLogTbl(sendObj, recId, fileName, true);
		Thread.sleep(5000);
	}
	
	private void onUpdateFileNameInLogTbl(Object obj, Long recId, String savedFileName, boolean status) {
		if (obj != null) {
			if (obj instanceof SharpGRInboundRootEleDTO) {
				mGoodsReceiptLogService.onUpdateFileNameInGoodsReceiptLog(recId, savedFileName, status);
			} else if (obj instanceof SharpShipmentInboundRootEleDTO) {
				mShipmentConfirmLogService.onUpdateFileNameInShipmentConfirmLog(recId, savedFileName, status);
			}
		}
	}

	private String writeXMLFile(Object obj, ChannelSftp sftpChannel, String flowRefNum) throws Exception {
		String remarks=null;
		JAXBContext jaxbContext = null;
		Marshaller marshaller = null;

		SharpGRInboundRootEleDTO sharpGRInboundRootEleDTO = null;
		SharpShipmentInboundRootEleDTO sharpShipmentInboundRootEleDTO = null;

		// Create a marshaller
		jaxbContext = JAXBContext.newInstance(obj.getClass());
		marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // For formatting

		// Convert the object to XML string
		StringWriter writer = new StringWriter();
		String saveSharpFileName = "";

		if (obj != null) {
			
			String docNum = getCrntDTTMinReqFormat(DATE_TIME_PATTERN);
			
			if (obj instanceof SharpGRInboundRootEleDTO) {
				sharpGRInboundRootEleDTO = (SharpGRInboundRootEleDTO) obj;
				sharpGRInboundRootEleDTO.getSharpGRInboundData().getSharpGREDI_DC40().setDocNumber(docNum);
				
				saveSharpFileName = new StringBuffer()
									.append(GR_FILE_NAME)
									.append(docNum)
									.toString();
				
				obj = sharpGRInboundRootEleDTO;
			} else if (obj instanceof SharpShipmentInboundRootEleDTO) {
				sharpShipmentInboundRootEleDTO = (SharpShipmentInboundRootEleDTO) obj;
				sharpShipmentInboundRootEleDTO.getIdoc().getEdi_dc40().setDocnum(docNum);
				
				saveSharpFileName = new StringBuffer()
									.append(SHIPMENT_CONFIRM_FILE_NAME)
									.append(docNum)
									.toString();
				
				obj = sharpShipmentInboundRootEleDTO;
			}else if (obj instanceof SharpAdjInboundRootEleDTO sharpAdjInboundRootEleDTO) {

				saveSharpFileName = new StringBuffer()
						.append(GM_ADJUSTMENT_FILE_NAME)
						.append(sharpAdjInboundRootEleDTO.getIdoc().getEdi_dc40().getDocnum())
						.toString();
				obj = sharpAdjInboundRootEleDTO;
			}

			marshaller.marshal(obj, writer);
		}

		String xmlData = writer.toString();

		String modifiedXmlData = removeXmlAttributes(xmlData, REMOVABLE_ATTRIBUTES_IN_XML);

		saveSharpFileName = new StringBuffer()
							.append(saveSharpFileName)
							.append(XML_FILE_EXTENSION)
							.toString();
		
		ByteArrayInputStream mByteArrayInputStream = null;
		
		try {
			mByteArrayInputStream = new ByteArrayInputStream(modifiedXmlData.getBytes());
			sftpChannel.put(mByteArrayInputStream, saveSharpFileName);
			//sftpChannel.put(new ByteArrayInputStream(modifiedXmlData.getBytes()), saveSharpFileName);
			
			if (isFileNameSaveInDB.equalsIgnoreCase("yes")) {
				onFileNameSaveInDBEnable(saveSharpFileName, true, flowRefNum);
			}
			
			remarks = new StringBuffer()
						.append("File saved successfully :: ")
						.append(saveSharpFileName)
						.toString();
			
			mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, OUTWARD);
			log.info("@@@ "+remarks+" @@@");
		}finally {
			if(mByteArrayInputStream != null) {
				mByteArrayInputStream.close();
			}
		}
		
		return saveSharpFileName;
	}
	
	private void onFileNameSaveInDBEnable(String crntFileName, boolean fileParsingStatus, String flowRefNum) {
		String remarks = "File name saved in DB Successfully";
		boolean fileNameSaveStatus = false;
		try {
			mProcessedFileEntryService.saveFileName(crntFileName, remarks, fileParsingStatus, OUTWARD);
			fileNameSaveStatus = true;
		} catch (Exception e) {
			remarks = e.getMessage();
		}
		
		remarks = (fileNameSaveStatus)?  remarks: "Exception in saving file name in DB:: "+remarks; 
		
		log.info(remarks);
		mAuditDataService.insertAuditData(remarks, flowRefNum, ((fileNameSaveStatus)?SUCCESS:FAILED), OUTWARD);
	}

}

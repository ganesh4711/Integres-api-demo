package com.integrations.orderprocessing.service;

import static com.integrations.orderprocessing.constants.StringConstants.FAILED;
import static com.integrations.orderprocessing.constants.StringConstants.INWARD;
import static com.integrations.orderprocessing.constants.StringConstants.SUCCESS;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.dto.DimScanResultDTO;
import com.integrations.orderprocessing.dto.DimScanResultData;
import com.integrations.orderprocessing.model.payload.UserCredentials;
import com.integrations.orderprocessing.model.req_body.freightsnap.DimScanner;
import com.integrations.orderprocessing.model.req_body.freightsnap.DimScannerDateTime;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class FreightSnapService {
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	AuditDataService mAuditDataService;
	
	@Autowired
	HttpService httpService;
	
	@Value("${app.fs.api.scanner_pro_data.url}")
    private String fs_scanner_pro_data_url;
	
	@Value("${app.fs.api.dimensioner_data.url}")
    private String fs_dimensioner_data_url;
    
    @Value("${app.fs.api.scanner_range_data.url}")
    private String fs_scanner_range_data_url;
  
	private JSONObject getFSUserCredentials(String flowRefNum) {
		log.info("**** In getFSUserCredentials ****");
    	String remarks=null;
    	JSONObject retResp =  null;
    	
    	UserCredentials  fsUserCredentials = applicationContext.getBean(UserCredentials.class);;
    	
    	try {
    		 retResp =  new JSONObject();
    		 retResp.put("userName", fsUserCredentials.getUsername());
    		 retResp.put("password", fsUserCredentials.getPassword());
		} catch (Exception e) {
			log.info("Exception in mapping FSUserCredentials to JSONObject:: "+e.getMessage());
		}
    	
    	return retResp;
	}
	
	public JSONObject getDimensionerData(DimScanner dimScanner, String reqType, String flowRefNum) {
		String reqURL = new StringBuffer()
				.append(fs_dimensioner_data_url)
				.append("?")
				.append("proNumber=")
				.append(dimScanner.getProNumber())
				.append("&")
				.append("weight=")
				.append(dimScanner.getWeight())
				.append("&")
				.append("sendImage=")
				.append(dimScanner.getSendImage())
				.toString();
		
		return onGetFreightSnapDimensionerData(reqURL, reqType, flowRefNum);
	}
	
   public JSONObject getDimScanData(DimScanner dimScanner, String reqType, String flowRefNum) {
		
		String reqURL=null;
		
		try {
			reqURL = new StringBuffer()
					.append(fs_scanner_pro_data_url)
					.append("?")
					.append("pro=")
					.append(dimScanner.getProNumber())
					.toString();
		} catch (Exception e) {
			log.error("Exception in parsing proNumber:: {}",e.getMessage());
		}
		
		return onGetFreightSnapScannerData(reqURL, reqType, flowRefNum);
	}
	
    public JSONObject getDateRangeDimScanData(DimScannerDateTime dimScannerDateTime, String reqType, String flowRefNum) {
    	String reqURL = new StringBuffer()
				.append(fs_scanner_range_data_url)
				.append("?")
				.append("start_date=")
				.append(dimScannerDateTime.getStartDate())
				.append("&")
				.append("start_time=")
				.append(dimScannerDateTime.getStartTime())
				.append("&")
				.append("end_date=")
				.append(dimScannerDateTime.getEndDate())
				.append("&")
				.append("end_time=")
				.append(dimScannerDateTime.getEndTime())
				.toString();
    	
    	return onGetFreightSnapScannerData(reqURL, reqType, flowRefNum);
	}
	
	private JSONObject onGetFreightSnapDimensionerData(String reqURL, String reqType, String flowRefNum) {
		boolean flowStatus = false;
		String remarks = null;
		
		JSONObject retResp = new JSONObject();
		retResp.put("status", flowStatus);
		retResp.put("data", null);
		
			try {
				
				remarks = "FreightSnap RequestURL(" + reqType + "):: " + reqURL;
				log.info(remarks);
				mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
				
				JSONObject resp = httpService.sendHttpGetRequest(reqURL, flowRefNum);
				
				remarks = "FreightSnap Response(" + reqType + "):: " + resp;
				log.info(remarks);
				mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
				flowStatus = true;
				
				retResp.put("status", flowStatus);
				retResp.put("data", onSetDimensionerData(resp));
				
			} catch (Exception e) {
				remarks = "Exception in requesting DimData from FreightSnap(" + reqType + "):: " + e.getMessage();
				log.info(remarks);
				mAuditDataService.insertAuditData(remarks, flowRefNum, (flowStatus)?SUCCESS:FAILED, INWARD);
				
				retResp.put("status", false);
				retResp.put("data", e.getMessage());
			}
		
		return retResp;
	}
	
	private JSONObject onGetFreightSnapScannerData(String reqURL, String reqType, String flowRefNum) {
		boolean flowStatus = false;
		String remarks = null;
		
		JSONObject retResp = new JSONObject();
		retResp.put("status", flowStatus);
		retResp.put("data", null);
		
		JSONObject credentials = getFSUserCredentials(flowRefNum);
		
		if(credentials != null) {
			try {
				
				remarks = "FreightSnap RequestURL(" + reqType + "):: " + reqURL;
				log.info(remarks);
				mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
				
				
				JSONObject resp = httpService.sendHttpGetRequestWithBasicAuthByStringResp(reqURL, flowRefNum,
						credentials.getString("userName"), credentials.getString("password"));
				
				remarks = "FreightSnap Response(" + reqType + "):: " + resp;
				log.info(remarks);
				mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
				flowStatus = true;
				
				DimScanResultDTO dimDTO = onSetDimData(resp);
				
				retResp.put("status", dimDTO.getError().size()>0?!flowStatus:flowStatus);
				retResp.put("data", dimDTO);
				
			} catch (Exception e) {
				remarks = "Exception in requesting DimData from FreightSnap(" + reqType + "):: " + e.getMessage();
				log.info(remarks);
				mAuditDataService.insertAuditData(remarks, flowRefNum, (flowStatus)?SUCCESS:FAILED, INWARD);
				
				retResp.put("status", false);
				retResp.put("data", e.getMessage());
			}
		}
		
		return retResp;
	}
	
	private DimScanResultDTO onSetDimData(JSONObject resp) {
		DimScanResultDTO dim = new DimScanResultDTO();
		
		List<DimScanResultData> result = new ArrayList<DimScanResultData>();
		JSONArray jsonResultArray = resp.getJSONArray("result");
		
		for(int i=0;i<jsonResultArray.size();i++) {
			
			JSONObject jsonResultObj = jsonResultArray.getJSONObject(i);
			
			DimScanResultData dimScanResultData = new DimScanResultData();
			dimScanResultData.setId(jsonResultObj.getString("id"));
			dimScanResultData.setCompany_id(jsonResultObj.getString("company_id"));
			dimScanResultData.setPro_number(jsonResultObj.getString("pro_number"));
			dimScanResultData.setScan_date("");
			dimScanResultData.setScan_time("");
			dimScanResultData.setScan_sequence("");
			dimScanResultData.setHeight(jsonResultObj.getString("height"));
			dimScanResultData.setLength(jsonResultObj.getString("length"));
			dimScanResultData.setWidth(jsonResultObj.getString("width"));
			dimScanResultData.setWeight(jsonResultObj.getString("weight"));
			dimScanResultData.setImage_count(jsonResultObj.getInt("image_count"));
			dimScanResultData.setEdims(jsonResultObj.getString("edims"));
			dimScanResultData.setCopies(jsonResultObj.getString("copies"));
			dimScanResultData.setScanned_terminal_id(jsonResultObj.getString("scanned_terminal_id"));
			dimScanResultData.setScanner_id(jsonResultObj.getString("scanner_id"));
			dimScanResultData.setUofm_dims(jsonResultObj.getString("uofm_dims"));
			dimScanResultData.setUofm_weight(jsonResultObj.getString("uofm_weight"));
			dimScanResultData.setTms_id(jsonResultObj.getString("tms_id"));
			dimScanResultData.setVolume("");
			dimScanResultData.setShip_date(jsonResultObj.getString("ship_date"));
			
			List<String> imgUrls = new ArrayList<String>();
			dimScanResultData.setImages_urls(imgUrls);
			
			JSONArray jsonImgArray = jsonResultObj.getJSONArray("images_urls");
	        for(int j=0;j<jsonImgArray.size();j++) {
				
				String jsonImgObj = jsonImgArray.getString(j);
				imgUrls.add(jsonImgObj);
			}
	        
	        List<String> answers = new ArrayList<String>();
			dimScanResultData.setAnswers(answers);
			
			JSONArray jsonAnsArray = jsonResultObj.getJSONArray("answers");
	        for(int j=0;j<jsonAnsArray.size();j++) {
				
				String jsonAnsObj = jsonAnsArray.getString(j);
				answers.add(jsonAnsObj);
			}
			
			result.add(dimScanResultData);
		}
		
		
		List<String> error = new ArrayList<String>();
		JSONArray jsonErrorArray = resp.getJSONArray("error");
		
        for(int i=0;i<jsonErrorArray.size();i++) {
			
			String jsonErrorObj = jsonErrorArray.getString(i);
			error.add(jsonErrorObj);
		}
		
		dim.setResult(result);
		dim.setError(error);
		
		
		return dim;
	}
	
	private DimScanResultDTO onSetDimensionerData(JSONObject resp) {
		DimScanResultDTO dim = new DimScanResultDTO();

		List<DimScanResultData> result = new ArrayList<DimScanResultData>();

		DimScanResultData dimScanResultData = new DimScanResultData();
		dimScanResultData.setId("");
		dimScanResultData.setCompany_id(resp.getString("companyID"));
		dimScanResultData.setPro_number(resp.getString("proNum"));
		dimScanResultData.setScan_date(resp.getString("scanDate"));
		dimScanResultData.setScan_time(resp.getString("scanTime"));
		dimScanResultData.setScan_sequence("");
		dimScanResultData.setHeight(resp.getString("ehdim"));
		dimScanResultData.setLength(resp.getString("eldim"));
		dimScanResultData.setWidth(resp.getString("ewdim"));
		dimScanResultData.setWeight(resp.getString("weight"));
		dimScanResultData.setEdims("");
		dimScanResultData.setCopies("");
		dimScanResultData.setScanned_terminal_id(resp.getString("scanTerminal"));
		dimScanResultData.setScanner_id(resp.getString("scannerID"));
		dimScanResultData.setUofm_dims("");
		dimScanResultData.setUofm_weight("");
		dimScanResultData.setTms_id("");
		dimScanResultData.setVolume(resp.getString("vol"));
		dimScanResultData.setShip_date("");

		List<String> imgUrls = new ArrayList<String>();
		dimScanResultData.setImages_urls(imgUrls);

		if (resp.containsKey("img")) {
			JSONArray jsonImgArray = resp.getJSONArray("img");
			dimScanResultData.setImage_count(jsonImgArray.size());
			for (int j = 0; j < jsonImgArray.size(); j++) {

				String jsonImgObj = jsonImgArray.getString(j);
				imgUrls.add(jsonImgObj);
			}
		}

		List<String> answers = new ArrayList<String>();
		dimScanResultData.setAnswers(answers);

		result.add(dimScanResultData);

		List<String> error = new ArrayList<String>();
		String errmsg = resp.getString("errmsg");
		error.add(errmsg);

		dim.setResult(result);
		dim.setError(error);

		return dim;
	}
	
}

package com.integrations.orderprocessing.service;

import com.integrations.orderprocessing.model.req_body.optioryx.Barcodes;
import jakarta.validation.Valid;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OptioryxService {
    @Autowired
    HttpService httpService;

    @Value("${api.key}")
    private String apiKey;

    @Value("${app.optioryx.get_items.url}")
    private String get_items_url;

    @Value("${app.optioryx.getItemByBarcodes.url}")
    private String get_Items_by_Barcode;
    @Value("${app.optioryx.getItemcount.url}")
    private String get_Items_Count;

    @Value("${app.optioryx.getItemById.url}")
    private String get_Item_by_Id;
    @Value("${app.optioryx.getItemCountByBarcode.url}")
    private String get_ItemCount_By_Barcode;





    public JSONObject getAllItems()  {
        JSONObject response = httpService.sendHttpGetRequestWithApikeyAuth(get_items_url, apiKey);
        return response ;


    }

    public JSONObject getItemByBarcodes(@Valid Barcodes barcodes) {
        JSONObject response=httpService.sendHttpGetRequestWithApikeyAuthandRequestBody(get_Items_by_Barcode,apiKey,barcodes);
        return response;
    }

    public JSONObject getItemsCount()  {
        return  httpService.sendHttpGetRequestWithApikey(get_Items_Count,apiKey);
    }

    public JSONObject getItemById(String id) {
        JSONObject response= httpService.sendHttpGetRequestWithApikeyAndPathvariable(id,get_Item_by_Id,apiKey);
        return response;
    }


    public JSONObject getItemCountByBarcode(Barcodes barcodes) {
        JSONObject itemcount=httpService.sendHttpPostRequestWithApikeyAuthandRequestBody(get_ItemCount_By_Barcode,apiKey,barcodes);
        return itemcount;
    }
}

package com.integrations.orderprocessing.service;

import static com.integrations.orderprocessing.constants.FleetEnableConstants.*;
import static com.integrations.orderprocessing.constants.SHARPConstants.*;
import static com.integrations.orderprocessing.constants.StringConstants.*;
import static com.integrations.orderprocessing.util.DateUtil.*;
import static com.integrations.orderprocessing.util.ObjectUtil.objectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.integrations.orderprocessing.kafka.producer.KafkaAvroProducer;
import com.integrations.orderprocessing.model.req_body.fleetenable.*;
import com.integrations.orderprocessing.secondary_ds.entity.Organization;
import com.integrations.orderprocessing.secondary_ds.repository.OrganizationRepository;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.integrations.orderprocessing.constants.FleetEnableConstants;
import com.integrations.orderprocessing.model.payload.FETokenPayload;
import com.integrations.orderprocessing.model.req_body.stackenable.AddressStackEnable;
import com.integrations.orderprocessing.model.req_body.stackenable.InwardChildStackEnable;
import com.integrations.orderprocessing.model.req_body.stackenable.InwardRootStackEnable;
import com.integrations.orderprocessing.model.req_body.stackenable.ItemDetailsStackEnable;
import com.integrations.orderprocessing.model.req_body.stackenable.OrderInfoStackEnable;
import com.integrations.orderprocessing.model.req_body.stackenable.OrderStackEnable;
import com.integrations.orderprocessing.primary_ds.entity.OrderItemLog;
import com.integrations.orderprocessing.primary_ds.entity.PurchaseOrderLog;
import com.integrations.orderprocessing.primary_ds.entity.ShipmentOutItemMaster;
import com.integrations.orderprocessing.primary_ds.entity.ShipmentOutMaster;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class FleetEnableService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    AuditDataService mAuditDataService;

    @Autowired
    HttpService httpService;

    @Autowired
    ApplicationContext applicationContext;

    @Value("${app.stack-enable.org-setup}")
    private String createOrgURL;

    @Value("${app.stack-enable.add-account-data}")
    private String addAccountDataURL;

    @Value("${app.stack-enable.del-account-data}")
    private String delAccountDataURL;

    @Value("${app.stack-enable.order-url}")
    private String createOrderURL;

    @Value("${app.stack-enable.order-delete-url}")
    private String deleteOrderURL;

    @Value("${app.fe.api.auth_token.url}")
    private String fe_auth_token_url;

    @Value("${app.fe.api.order_save.url}")
    private String fe_order_save_url;

    @Value("${app.fe.api.wms_data_save.url}")
    private String fe_wms_data_save_url;


    @Autowired
    private PurchaseOrderLogService purchaseOrderLogService;

    @Autowired
    private ShipmentoutLogService shipmentoutLogService;

    @Autowired
    private KafkaAvroProducer kafkaAvroProducer;

    public boolean onIntegrationSetup(FEOrgData feData, String reqType, String flowRefNum) {
        String remarks = null;
        boolean flowStatus = false;
        try {
            JSONObject reqJsonPayload = onGetStackEnableOrgData(feData, flowRefNum);
            String orgpayload = reqJsonPayload.toString();
            FEOrgData feOrgData = objectMapper.readValue(orgpayload, FEOrgData.class);
            kafkaAvroProducer.sendDataOnTopicFleetEnableOrgSetup(feOrgData,flowRefNum);
            remarks = "Storing Organization Data in topic is success";
            flowStatus = true;
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
            log.info(remarks);
            //return onPostStackEnableData(reqJsonPayload, createOrgURL, reqType, flowRefNum);
        } catch (Exception e) {
            remarks = "Storing Organization  Data in topic is failed";
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
            log.info(remarks);
        }
        return flowStatus;

    }

    public boolean onAccountCreated(JSONObject reqJsonPayload, String reqType, String flowRefNum) {
        String remarks = null;
        boolean flowStatus = false;
        try {
            String accountPayload = reqJsonPayload.toString();
            FEAccountCreatedEvent feAccountCreatedEvent = objectMapper.readValue(accountPayload, FEAccountCreatedEvent.class);
            kafkaAvroProducer.sendDataOnTopicFleetEnableAccountSetup(feAccountCreatedEvent,flowRefNum);
            remarks = "Storing Account creation Data in topic is success";
            flowStatus = true;
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
            log.info(remarks);
        } catch (Exception e) {
            remarks = "Storing Account creation  Data in topic is failed";
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
            log.info(remarks);
        }
        return flowStatus;

//        return onPostStackEnableData(reqJsonPayload, addAccountDataURL, reqType, flowRefNum);
    }

    public boolean onAccountUpdated(JSONObject reqJsonPayload, String reqType, String flowRefNum) {
        String remarks = null;
        boolean flowStatus = false;
        try {
            String accountPayload = reqJsonPayload.toString();
            FEAccountUpdatedEvent feAccountUpdatedEvent = objectMapper.readValue(accountPayload, FEAccountUpdatedEvent.class);
            kafkaAvroProducer.sendDataOnTopicFleetEnableAccountSetup(feAccountUpdatedEvent, flowRefNum);
            remarks = "Storing Account updation Data in topic is success";
            flowStatus = true;
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
            log.info(remarks);
        } catch (Exception e) {
            remarks = "Storing Account updation  Data in topic is failed";
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
            log.info(remarks);
        }
        return flowStatus;
//        return onPostStackEnableData(reqJsonPayload, addAccountDataURL, reqType, flowRefNum);
    }

    public boolean onAccountDeleted(JSONObject reqJsonPayload, String reqType, String flowRefNum) {

        String remarks = null;
        boolean flowStatus = false;
        try {
            String delaccountPayload = reqJsonPayload.toString();
            FEAccountDeleteEvent feAccountDeleteEvent = objectMapper.readValue(delaccountPayload, FEAccountDeleteEvent.class);
            kafkaAvroProducer.sendDataOnTopicFleetEnableAccountDelete(feAccountDeleteEvent,flowRefNum);
            remarks = "Storing Account deletion Data in topic is success";
            flowStatus = true;
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
            log.info(remarks);
        } catch (Exception e) {
            remarks = "Storing Account deletion  Data in topic is failed";
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
            log.info(remarks);
        }
        return flowStatus;

//        return onDeleteStackEnableData(reqJsonPayload, delAccountDataURL, reqType, flowRefNum);
    }

    public boolean onOrderCreated(FEOrderData feOrderData, String reqType, String flowRefNum) {

        return onOrderCreateOrUpdate(feOrderData, createOrderURL, reqType, flowRefNum);
    }

    public boolean onOrderUpdated(FEOrderData feOrderData, String reqType, String flowRefNum) {
        return onOrderCreateOrUpdate(feOrderData, createOrderURL, reqType, flowRefNum);
    }

    private boolean onOrderCreateOrUpdate(FEOrderData feOrderData, String url, String reqType, String flowRefNum) {

        onSaveFleetOrderInLog(feOrderData);
        JSONObject reqJsonPayload = onGetStackEnableOrderData(feOrderData, flowRefNum);
        if (reqJsonPayload != null) {
            InwardRootStackEnable rootStackEnable = null;
            try {
                rootStackEnable = objectMapper.readValue(reqJsonPayload.toString(), InwardRootStackEnable.class);

                kafkaAvroProducer.sendDataOnTopicFeOrder(rootStackEnable, reqType,flowRefNum);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean onOrderDeleted(JSONObject reqJsonPayload, String reqType, String flowRefNum) {
        String remarks = null;
        boolean flowStatus = false;
        try {
            String orderdelPayload = reqJsonPayload.toString();
            FEOrderDeletedEvent feOrderDeletedEvent = objectMapper.readValue(orderdelPayload, FEOrderDeletedEvent.class);
            kafkaAvroProducer.sendDataOnTopicFleetEnableOrderDelete(feOrderDeletedEvent,flowRefNum);
            remarks = "Storing Order deletion Data in topic is success";
            flowStatus = true;
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
            log.info(remarks);
        } catch (Exception e) {
            remarks = "Storing Order deletion  Data in topic is failed";
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
            log.info(remarks);
        }
        return flowStatus;


//		return onPostStackEnableData(reqJsonPayload, deleteOrderURL, reqType, flowRefNum);
    }

    private boolean onPostStackEnableData(JSONObject reqJsonPayload, String reqURL, String reqType, String flowRefNum) {
        boolean flowStatus = false;
        String remarks = null;
        JSONObject resp = null;

        try {

            if (reqType.equals("onOrderCreated") || reqType.equals("onOrderUpdated") || reqType.equals("onIntegrationSetup")) {
                remarks = "StackEnable RequestBody(" + reqType + "):: " + reqJsonPayload;
                log.info(remarks);
                mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
            }

            remarks = "StackEnable RequestURL(" + reqType + "):: " + reqURL;
            log.info(remarks);
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);

            resp = httpService.sendHttpPostRequest(reqURL, reqJsonPayload, flowRefNum);

            remarks = "StackEnable Response(" + reqType + "):: " + resp;
            log.info(remarks);
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);

            flowStatus = true;
        } catch (Exception e) {
            remarks = "Exception in sending Data to StackEnable(" + reqType + "):: " + e.getMessage();
            log.info(remarks);
            mAuditDataService.insertAuditData(remarks, flowRefNum, (flowStatus) ? SUCCESS : FAILED, INWARD);
        }

        return flowStatus;
    }

    private boolean onDeleteStackEnableData(JSONObject reqJsonPayload, String reqURL, String reqType, String flowRefNum) {
        boolean flowStatus = false;
        String remarks = null;
        JSONObject resp = null;

        try {

            remarks = "StackEnable RequestURL(" + reqType + "):: " + reqURL;
            log.info(remarks);
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);

            resp = httpService.sendHttpDeleteRequest(reqURL, reqJsonPayload, flowRefNum);

            remarks = "StackEnable Response(" + reqType + "):: " + resp;
            log.info(remarks);
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);

            flowStatus = true;
        } catch (Exception e) {
            remarks = "Exception in sending Data to StackEnable(" + reqType + "):: " + e.getMessage();
            log.info(remarks);
            mAuditDataService.insertAuditData(remarks, flowRefNum, (flowStatus) ? SUCCESS : FAILED, INWARD);
        }

        return flowStatus;
    }

    private JSONObject onGetStackEnableOrgData(FEOrgData feData, String flowRefNum) {
        JSONObject reqJsonPayload = null;
        String remarks = null;

        try {

            FECarrierSetup feCarrierSetup = feData.getCarrier();
            feCarrierSetup.setOrganization_sub_type(feCarrierSetup.getOrganization_type());
            feCarrierSetup.setOrganization_type(FE_ORG_TYPE_CARRIER);

            reqJsonPayload = JSONObject.fromObject(objectMapper.writeValueAsString(feData));
        } catch (Exception e) {
            remarks = "Exception in preparing StackEnable Order Payload:: " + e.getMessage();
            log.info(remarks);
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
        }

        return reqJsonPayload;
    }

    private JSONObject onGetStackEnableOrderData(FEOrderData feOrderData, String flowRefNum) {
        JSONObject reqJsonPayload = null;
        String remarks = null;
        try {
            InwardRootStackEnable root = new InwardRootStackEnable();

            List<InwardChildStackEnable> orders = new ArrayList<InwardChildStackEnable>();
            root.setOrders(orders);

            InwardChildStackEnable child = new InwardChildStackEnable();
            orders.add(child);

            OrderStackEnable order = new OrderStackEnable();
            String orderType = onSetStackEnableOrder(feOrderData, order); // Set Order

            FEOrder feOrder = feOrderData.getOrder();
            OrderInfoStackEnable orderInfo = new OrderInfoStackEnable();
            onSetStackEnableOrderInfo(feOrder, orderInfo); // Set OrderInfo

            List<ItemDetailsStackEnable> itemDetails = new ArrayList<ItemDetailsStackEnable>();
            onSetStackEnableItemDetails(feOrder.getItem_details(), itemDetails, orderType); // Set Item Details

            child.setOrder(order);
            child.setOrderInfo(orderInfo);
            child.setItemDetails(itemDetails);

            if (orderType.equals(TRANSFER)) {
                AddressStackEnable orgnAddress = order.getOriginAddress();
                AddressStackEnable destAddress = order.getDestinationAddress();

                Optional<Organization> orgnOrg = organizationRepository.fetchOrganizationByName(orgnAddress.getOrgName());
                Optional<Organization> destOrg = organizationRepository.fetchOrganizationByName(destAddress.getOrgName());

                if ((orgnOrg.isPresent() && orgnOrg.get().getOrganizationType().getValue() == 2)
                        && (destOrg.isPresent() && destOrg.get().getOrganizationType().getValue() == 2)) {
                    order.setWareHouseName(orgnAddress.getOrgName());
                    order.setOrderType(TRANSFER_OUTWARD);

                    InwardChildStackEnable clonedOrder = (InwardChildStackEnable) SerializationUtils.clone(orders.get(0));
                    clonedOrder.getOrder().setOrderType(TRANSFER_INWARD);
                    clonedOrder.getOrder().setWareHouseName(destAddress.getOrgName());
                    clonedOrder.getOrder().setOriginAddress(destAddress);
                    clonedOrder.getOrder().setDestinationAddress(orgnAddress);

                    if (feOrderData.getEvent().equals(ORDER_UPDATED)) {
                        if (feOrder.getStop_type().equals(FE_ORDER_STOP_TYPE_PICKUP) && clonedOrder.getOrder().getOrderType().equals(TRANSFER_INWARD)) {
                            clonedOrder.getOrder().setOrderReceivedDate(feOrder.getActual_end_date_time());

                        } else if (feOrder.getStop_type().equals(FE_ORDER_STOP_TYPE_DELIVERY) && order.getOrderType().equals(TRANSFER_OUTWARD)) {
                            order.setOrderDispatchedDate(feOrder.getActual_start_date_time());

                        }

                    }
                    orders.add(clonedOrder);
                } else {
                    remarks = "Failed to send Transfer order data to StackEnable. " +
                            (orgnOrg.isPresent() ? "Available" : "Unavailable") +
                            " Origin organization" +
                            ": " + orgnAddress.getOrgName() +
                            ". " +
                            (destOrg.isPresent() ? "Available" : "Unavailable") +
                            " Destination organization: " + destAddress.getOrgName() + ".";

                    log.info(remarks);

                    mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);

                    return null;
                }
            } else {
                order.setOrderType(orderType);
            }

            reqJsonPayload = JSONObject.fromObject(objectMapper.writeValueAsString(root));
        } catch (Exception e) {
            remarks = "Exception in preparing StackEnable Order Payload:: " + e.getMessage();
            log.info(remarks);
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
        }

        return reqJsonPayload;
    }

    private String onSetStackEnableOrder(FEOrderData feOrderData, OrderStackEnable order) {

        FEOrder feOrder = feOrderData.getOrder();

        order.setSource(FE_SOURCE_NAME);
        order.setOrderId(feOrder.getId());
        order.setOrderNumber(feOrder.getCustomer_order_number());

        String orderType = getStackEnableOrderType(feOrder.getOrder_type());

        order.setHawbNumber(feOrder.getHawb());
        order.setMawbNumber(feOrder.getMawb());

        order.setOrderStatus(FE_TO_SE_ORDER_STATUS_MAPPER.get(feOrder.getStatus()));

        order.setCarrierName(feOrder.getCarrier_name());
        order.setWareHouseName(feOrder.getWarehouse_name());
        order.setShipperName(feOrder.getShipper_name());

        order.setReferenceNumber(feOrder.getReference_number());
        order.setActionCode(getOrderActionCode(feOrderData.getEvent()));

        AddressStackEnable originAddress = new AddressStackEnable();
        onSetStackEnableAddress(feOrder.getOriginAddress(), originAddress);
        order.setOriginAddress(originAddress);

        AddressStackEnable destinationAddress = new AddressStackEnable();
        onSetStackEnableAddress(feOrder.getDestinationAddress(), destinationAddress);
        order.setDestinationAddress(destinationAddress);

//        String receivedDate = feOrder.getReceived_at();
//        if (feOrderData.getEvent().equals(ORDER_UPDATED)){
//            order.setOrderReceivedDate(receivedDate);
//        }
//        if (orderType.equals(INWARD)) {
//            order.setOrderReceivedDate(receivedDate);
//        } else if (orderType.equals(OUTWARD)) {
//            order.setOrderDispatchedDate(receivedDate);
//        } else {
//            order.setOrderReceivedDate(receivedDate);
//        }

        if (feOrderData.getEvent().equals(ORDER_UPDATED)) {
            if (orderType.equals(INWARD)||orderType.equals(SINGLE_DAY_IN)) {
                order.setOrderReceivedDate(feOrder.getActual_end_date_time());
            } else if (orderType.equals(OUTWARD)||orderType.equals(SINGLE_DAY_OUT)) {
                order.setOrderDispatchedDate(feOrder.getActual_start_date_time());
            }

        }


        return orderType;

    }

    private String getOrderActionCode(String eventType) {
        String retData = "";
        switch (eventType) {
            case "order.created":
                retData = "001";
                break;
            case "order.updated":
                retData = "002";
                break;
        }

        return retData;
    }


    private String getStackEnableOrderType(String orderTypeCode) {
        String retData = "";
        switch (orderTypeCode) {
            case FE_ORDER_TYPE_SIGLE_DAY_DELIVERY:
                retData = SINGLE_DAY_OUT;
                break;
            case FE_ORDER_TYPE_SINGLE_DAY_RETURN:
                retData = SINGLE_DAY_IN;
                break;
            case FE_ORDER_TYPE_MULTIDAY_DELIVERY:
                retData = OUTWARD;
                break;
            case FE_ORDER_TYPE_MULTIDAY_RETURN:
            case FE_ORDER_TYPE_MULTIDAY_PICKUP:
                retData = INWARD;
                break;
            case FE_ORDER_TYPE_TRANSFER:
                retData = TRANSFER;
                break;
            case FE_ORDER_TYPE_SINGLE_DAY_MOVE:
                retData = "";
                break;
            case FE_ORDER_TYPE_MULTIDAY_DAY_MOVE:
                retData = "";
                break;
        }

        return retData;
    }

    private void onSetStackEnableAddress(FEOrderAddress feAddress, AddressStackEnable seAddress) {
        seAddress.setOrgName(feAddress.getOrgName());
        seAddress.setContactName(feAddress.getContactName());
        seAddress.setAddress1(feAddress.getAddress1());
        seAddress.setAddress2(feAddress.getAddress2());
        seAddress.setCity(feAddress.getCity());
        seAddress.setState(feAddress.getState());
        seAddress.setCountry(feAddress.getCountry());
        seAddress.setPostal(feAddress.getPostal());
        seAddress.setPhone(feAddress.getPhone());
        seAddress.setEmail(feAddress.getEmail());
        seAddress.setFax(feAddress.getFax());
        seAddress.setLatitude(feAddress.getLatitude());
        seAddress.setLongitude(feAddress.getLongitude());
        seAddress.setRequested_date(feAddress.getRequestedDate());
    }

    private void onSetStackEnableOrderInfo(FEOrder feOrder, OrderInfoStackEnable orderInfo) {
        orderInfo.setWeight(feOrder.getWeight() + "");
        orderInfo.setNumberOfPallets(feOrder.getPallets());
        orderInfo.setQuantity(feOrder.getQuantity());
        orderInfo.setOrderDate(feOrder.getReceived_at());
        orderInfo.setTruckNumber("");// Value need to be mapped on condition based
    }

    private void onSetStackEnableItemDetails(List<FEOrderItem> item_details, List<ItemDetailsStackEnable> itemDetails, String orderType) {
        for (FEOrderItem fOrderItem : item_details) {
            ItemDetailsStackEnable item = getStackEnableItem(fOrderItem, orderType);
            itemDetails.add(item);
        }
    }

    private ItemDetailsStackEnable getStackEnableItem(FEOrderItem fOerderItem, String orderType) {
        ItemDetailsStackEnable item = new ItemDetailsStackEnable();

        item.setCommodityId(fOerderItem.getItem_id());
        item.setProductName(fOerderItem.getItem_name());
        item.setQuantity(fOerderItem.getItem_quantity());
        item.setWeight(fOerderItem.getItem_weight());
        item.setWeightUnit(fOerderItem.getItem_weight_uom());
        item.setDockLocation(fOerderItem.getWh_dock());
        item.setSkuId(fOerderItem.getSerial_number());
        item.setItemStatus(fOerderItem.getItem_status());
        item.setDimensions(fOerderItem.getItem_length() + "*" + fOerderItem.getItem_width() + "*"
                + fOerderItem.getItem_height());
        item.setItemId(fOerderItem.getItem_id());

        return item;
    }

    // Send Data to FleetEnable
    private String getFEToken(String flowRefNum) {
        log.info("**** In getFEToken ****");
        String token = null;
        String remarks = null;
        JSONObject reqBody = null;

        FETokenPayload feTokenPayload = applicationContext.getBean(FETokenPayload.class);
        ;

        try {
            reqBody = JSONObject.fromObject(objectMapper.writeValueAsString(feTokenPayload));
        } catch (JsonProcessingException e) {
            log.info("Exception in mapping FETokenPayload to JSONObject ReqBody:: " + e.getMessage());
        }

        if (reqBody != null) {
            remarks = new StringBuffer()
                    .append("FleetEnable-> Request URL(Token):: ")
                    .append(fe_auth_token_url).append(" ## Request Payload:: ")
                    .append(reqBody).toString();
            log.info(remarks);
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, OUTWARD);

            try {

                JSONObject repObj = httpService.sendHttpPostRequest(fe_auth_token_url, reqBody, flowRefNum);

                if (repObj != null) {
                    token = repObj.getString("token");
                }
            } catch (Exception e) {
                log.info("Exception in getting RR Token:: " + e.getMessage());
            }
        }

        return token;
    }

    public boolean onSendFleetEnableOrderData(JSONObject reqJsonPayload, String reqType, String flowRefNum) {
        boolean flowStatus=false;
        String remarks=null;
        try {
            String feordersave = reqJsonPayload.toString();
            FEOrderSave feOrderSave = objectMapper.readValue(feordersave, FEOrderSave.class);
            kafkaAvroProducer.sendDataOnTopicStackEnableFEOrderSave(feOrderSave,flowRefNum);
            flowStatus=true;
            remarks= "Feordersave data saved  in topic successfully";
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
            log.info(remarks);
        } catch (Exception e) {
            remarks = "Feordersave  data failed to save in the topic";
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
            log.info(remarks);
        }
        return flowStatus;

//        return onSendFleetEnableData(reqJsonPayload, fe_order_save_url, reqType, flowRefNum);
    }

    public boolean onSendFleetEnableWMSData(JSONObject reqJsonPayload, String reqType, String flowRefNum) {
        boolean flowStatus=false;
        String remarks=null;
        try {
            String wmsdata = reqJsonPayload.toString();
            FEwmsDataSave fEwmsDataSave = objectMapper.readValue(wmsdata, FEwmsDataSave.class);
            kafkaAvroProducer.sendDataOnTopicWMSData(fEwmsDataSave,flowRefNum);
            flowStatus=true;
            remarks= "wmsdata saved  in topic successfully ";
            mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
            log.info(remarks);
        } catch (Exception e) {
            remarks = "wmsdata data failed to save in the topic";
            mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, INWARD);
            log.info(remarks);
        }
        return flowStatus;
//        return onSendFleetEnableData(reqJsonPayload, fe_wms_data_save_url, reqType, flowRefNum);
    }

    private JSONObject onSendFleetEnableData(JSONObject reqJsonPayload, String reqURL, String reqType, String flowRefNum) {
        boolean flowStatus = false;
        String remarks = null;

        JSONObject retResp = new JSONObject();
        retResp.put("status", flowStatus);
        retResp.put("data", null);

        String token = getFEToken(flowRefNum);

        if (token != null) {
            try {

                remarks = "FleetEnable RequestURL(" + reqType + "):: " + reqURL;
                log.info(remarks);
                mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);

                JSONObject resp = httpService.sendHttpPostRequestWithTokenAuth(reqURL, reqJsonPayload, flowRefNum, token);

                remarks = "FleetEnable Response(" + reqType + "):: " + resp;
                log.info(remarks);
                mAuditDataService.insertAuditData(remarks, flowRefNum, SUCCESS, INWARD);
                flowStatus = true;

                if (reqType.equals("onSendFleetEnableOrderData")) {
                    retResp.put("status", (resp.containsKey("ackId")) ? true : false);
                } else {
                    retResp.put("status", flowStatus);
                }

                retResp.put("data", resp);

            } catch (Exception e) {
                remarks = "Exception in sending Data to FleetEnable(" + reqType + "):: " + e.getMessage();
                log.info(remarks);
                mAuditDataService.insertAuditData(remarks, flowRefNum, (flowStatus) ? SUCCESS : FAILED, INWARD);

                retResp.put("data", remarks);
            }
        } else {
            remarks = "Issue in auth token service";
            retResp.put("data", remarks);
        }

        return retResp;
    }

    private void onSaveFleetOrderInLog(FEOrderData feOrderData) {
        String orderType = getStackEnableOrderType(feOrderData.getOrder().getOrder_type());
        String actionCode = getOrderActionCode(feOrderData.getEvent());
        if (INWARD.equals(orderType) || TRANSFER.equals(orderType)) {
            PurchaseOrderLog orderLog = onPreparePurchaseOrderLogData(feOrderData, orderType, actionCode);
            purchaseOrderLogService.saveOrderLog(orderLog);

        } else {
            ShipmentOutMaster shipmentOutMaster = onPrepareShipmentOutMasterData(feOrderData, actionCode);
            shipmentoutLogService.saveOrderLog(shipmentOutMaster);
        }
    }

    private PurchaseOrderLog onPreparePurchaseOrderLogData(FEOrderData feOrderData, String orderType, String actionCode) {
        PurchaseOrderLog orderLog = new PurchaseOrderLog();

        FEOrder order = feOrderData.getOrder();

        List<OrderItemLog> orderItemLogList = createOrderItemLog(feOrderData, actionCode, orderLog);

        orderLog.setOrderNumber(order.getCustomer_order_number());
        orderLog.setCreated_date(convertToDTTMtoDTTM(feOrderData.getOrder().getReceived_at(), DATE_TIME_PATTERN8, DATE_PATTERN));
        orderLog.setCreated_time(convertToDTTMtoTM(feOrderData.getOrder().getReceived_at(), DATE_TIME_PATTERN8, TIME_PATTERN));
        orderLog.setRequestType(ORDER);
        orderLog.setSourcePartner(FleetEnableConstants.FE_SOURCE_NAME);
        orderLog.setFlowType(orderType);
        orderLog.setDestinationPartner(order.getWarehouse_name());
        orderLog.setOrderItemsLog(orderItemLogList);
        switch (actionCode) {
            case PO_ITEM_ACTION_ADD_QLFR:
                orderLog.setStatus(CREATE);
                break;
            case PO_ITEM_ACTION_UPDATE_QLFR:
                orderLog.setStatus(UPDATE);
                break;
            case PO_ITEM_ACTION_CANCELLED_QLFR:
                orderLog.setStatus(DELETE);
                break;
        }
        return orderLog;

    }

    private List<OrderItemLog> createOrderItemLog(FEOrderData order, String actionCode, PurchaseOrderLog purchaseOrderLog) {
        List<OrderItemLog> orderItemsLogList = new ArrayList<>();

        List<FEOrderItem> feOrderItems = order.getOrder().getItem_details();

        for (FEOrderItem feOrderItem : feOrderItems) {
            OrderItemLog orderItemLog = new OrderItemLog();

            orderItemLog.setPurchaseOrderLog(purchaseOrderLog);
            orderItemLog.setItemNumber(feOrderItem.getItem_id());

            orderItemLog.setAction(actionCode);
            orderItemLog.setCreated_date(convertToDTTMtoDTTM(order.getOrder().getReceived_at(), DATE_TIME_PATTERN8, DATE_PATTERN));
            orderItemLog.setCreated_time(convertToDTTMtoTM(order.getOrder().getReceived_at(), DATE_TIME_PATTERN8, TIME_PATTERN));
            orderItemLog.setQuantity(feOrderItem.getItem_quantity());
            orderItemLog.setProductId(feOrderItem.getItem_model());
            orderItemLog.setWarehouseId(order.getOrder().getWarehouse_id());
            switch (actionCode) {
                case PO_ITEM_ACTION_ADD_QLFR:
                    orderItemLog.setStatus(CREATE);
                    break;
                case PO_ITEM_ACTION_UPDATE_QLFR:
                    orderItemLog.setStatus(UPDATE);
                    break;
                case PO_ITEM_ACTION_CANCELLED_QLFR:
                    orderItemLog.setStatus(DELETE);
                    break;
            }

            orderItemsLogList.add(orderItemLog);
        }

        return orderItemsLogList;
    }

    private ShipmentOutMaster onPrepareShipmentOutMasterData(FEOrderData feOrderData, String actionCode) {
        ShipmentOutMaster shipmentOutMaster = new ShipmentOutMaster();
        FEOrder order = feOrderData.getOrder();
        List<FEOrderItem> itemDetails = order.getItem_details();

        List<ShipmentOutItemMaster> outItemMasters = new ArrayList<>();

        for (FEOrderItem feOrderItem : itemDetails) {
            ShipmentOutItemMaster shipmentOutItemMaster = createShipmentOutItemMaster(feOrderItem, order, shipmentOutMaster);
            outItemMasters.add(shipmentOutItemMaster);
        }

        shipmentOutMaster.setShipmentNumber("");
        shipmentOutMaster.setDeliveryNumber("");
        shipmentOutMaster.setDestinationPartner("");
        shipmentOutMaster.setCreated_date(convertToDTTMtoDTTM(feOrderData.getOrder().getReceived_at(), DATE_TIME_PATTERN8, DATE_PATTERN));
        shipmentOutMaster.setCreated_time(convertToDTTMtoTM(feOrderData.getOrder().getReceived_at(), DATE_TIME_PATTERN8, TIME_PATTERN));
        shipmentOutMaster.setFlowType(INWARD);
        shipmentOutMaster.setReferenceNumber2(feOrderData.getOrder().getReference_number());
        shipmentOutMaster.setSourcePartner(order.getWarehouse_name());
        switch (actionCode) {
            case PO_ITEM_ACTION_ADD_QLFR:
                shipmentOutMaster.setStatus(CREATE);
                break;
            case PO_ITEM_ACTION_UPDATE_QLFR:
                shipmentOutMaster.setStatus(UPDATE);
                break;
            case PO_ITEM_ACTION_CANCELLED_QLFR:
                shipmentOutMaster.setStatus(DELETE);
                break;
        }
        shipmentOutMaster.setShipmentOutItemMasterList(outItemMasters);

        return shipmentOutMaster;


    }

    private ShipmentOutItemMaster createShipmentOutItemMaster(FEOrderItem feOrderItem, FEOrder order, ShipmentOutMaster shipmentOutMaster) {


        ShipmentOutItemMaster shipmentOutItemMaster = new ShipmentOutItemMaster();

        shipmentOutItemMaster.setCreated_date(convertToDTTMtoDTTM(order.getReceived_at(), DATE_TIME_PATTERN8, DATE_PATTERN));
        shipmentOutItemMaster.setCreated_time(convertToDTTMtoTM(order.getReceived_at(), DATE_TIME_PATTERN8, TIME_PATTERN));

        shipmentOutItemMaster.setItemNumber(feOrderItem.getItem_id());
        shipmentOutItemMaster.setWarehouseId(order.getWarehouse_id());
        shipmentOutItemMaster.setQuantity(feOrderItem.getItem_quantity());
        shipmentOutItemMaster.setStorageLocation(feOrderItem.getWh_dock());
        shipmentOutItemMaster.setValuationType(null);
        shipmentOutItemMaster.setProductId(feOrderItem.getItem_name());
//		shipmentOutItemMaster.setWarehouseName(order.getWarehouse_name());
        shipmentOutItemMaster.setShipmentOutMaster(shipmentOutMaster);

        return shipmentOutItemMaster;
    }


}

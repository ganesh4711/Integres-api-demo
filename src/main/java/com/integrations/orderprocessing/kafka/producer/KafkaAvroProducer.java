package com.integrations.orderprocessing.kafka.producer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.integrations.orderprocessing.model.req_body.fleetenable.*;
import com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments.ProductAdjustmentDto;
import com.integrations.orderprocessing.model.req_body.stackenable.InwardRootStackEnable;
import com.integrations.orderprocessing.service.AuditDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.kafka.schema.GoodsReceiptData;
import com.integrations.orderprocessing.kafka.schema.ShpmntConfInData;

import lombok.extern.slf4j.Slf4j;

import static com.integrations.orderprocessing.constants.StringConstants.*;

@Slf4j
@Service
public class KafkaAvroProducer {

	@Value("${app.kafka.topic.stackenable-gr}")
    private String topicStackEnbleGR;
	
	@Value("${app.kafka.topic.stackenable-sc}")
    private String topicStackEnbleSC;

    @Value("${app.kafka.topic.stackenable-adj}")
    private String topicStackEnbleAdj;
    @Value("${app.kafka.topic.stackenable-order}")
    private String topicStackEnbleOrder;
    @Value("${app.kafka.topic.fleetenable-order}")
    private String topicFleetEnableOrder;

    @Value("${app.kafka.topic.fleetenable-org-setup}")
    private String topicFleetEnableOrgSetup;

    @Value("${app.kafka.topic.fleetenable-account-setup}")
    private String topicFleetEnableAccountCreate;


    @Value("${app.kafka.topic.fleetenable-account-delete}")
    private String topicFleetEnableAccountDelete;

    @Value("${app.kafka.topic.fleetenable-order-delete}")
    private String topicFleetEnableOrderDelete;

    @Value("${app.kafka.topic.stackenable-order-save}")
    private  String topicStackEnableOrderSave;

    @Value("${app.kafka.topic.stackenable-wms-data-setup}")
    private String topicWMSData;
    @Autowired
    private KafkaTemplate<String,FEwmsDataSave> templateWmsData;
    @Autowired
    private KafkaTemplate<String,FEOrderSave> templateFEOrderSave;
    @Autowired
    private  KafkaTemplate<String,FEOrderDeletedEvent> templateOrderDelete;


    @Autowired
    private KafkaTemplate<String,FEAccountDeleteEvent> templateAccountDelete;



    @Autowired
    private KafkaTemplate<String, FEAccountCreatedEvent> templateAccountSetup;

    @Autowired
    private KafkaTemplate<String, FEOrgData> templateOrgSetup;


    @Autowired
    private KafkaTemplate<String, GoodsReceiptData> templateGRSchema;
    
    @Autowired
    private KafkaTemplate<String, com.integrations.orderprocessing.model.req_body.inbound.gr.GoodsReceiptData> templateGR;
    @Autowired
    private KafkaTemplate<String, com.integrations.orderprocessing.model.req_body.stackenable.InwardRootStackEnable> orderTemplate;

    @Autowired
    private KafkaTemplate<String, ShpmntConfInData> templateSCSchema;
    
    @Autowired
    private KafkaTemplate<String, com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm.ShpmntConfInData> templateSC;
    @Autowired
    private KafkaTemplate<String, com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments.ProductAdjustmentDto> templateAdj;

    @Autowired
    AuditDataService mAuditDataService;

    public void sendDataOnTopicStackEnbleGR(com.integrations.orderprocessing.model.req_body.inbound.gr.GoodsReceiptData grData, String flowRefNum){
    		CompletableFuture<SendResult<String, com.integrations.orderprocessing.model.req_body.inbound.gr.GoodsReceiptData>> future = templateGR.send(topicStackEnbleGR, flowRefNum, grData);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    String remarks = "Sent message with offset=[" + result.getRecordMetadata().offset()
                            + "] and partition=[" + result.getRecordMetadata().partition()
                            + "] and message=[" + grData
                            + "]";
                	log.info(remarks);
                    mAuditDataService.insertAuditData(remarks,flowRefNum,FAILED, TOPIC);
                } else {
                	 log.info("Unable to send message=[" +
                			 grData + "] due to : " + ex.getMessage());
                }
            });
    }
    
    
    public void sendDataOnTopicStackEnbleSC(com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm.ShpmntConfInData shpmntConfInData, String flowRefNum){
		CompletableFuture<SendResult<String, com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm.ShpmntConfInData>> future = templateSC.send(topicStackEnbleSC, flowRefNum, shpmntConfInData);
		future.whenComplete((result, ex) -> {
            if (ex == null) {
                String remarks = "Sent message with offset=[" + result.getRecordMetadata().offset()
                        + "] and partition=[" + result.getRecordMetadata().partition()
                        + "] and message=[" + shpmntConfInData
                        + "]";
            	log.info(remarks);
                mAuditDataService.insertAuditData(remarks,flowRefNum,FAILED,TOPIC);
            } else {
            	 log.info("Unable to send message=[" +
            			 shpmntConfInData + "] due to : " + ex.getMessage());
            }
        });
}
    
        public void sendDataOnTopicStackEnbleGR(GoodsReceiptData grData){
        		CompletableFuture<SendResult<String, GoodsReceiptData>> future = templateGRSchema.send(topicStackEnbleGR, UUID.randomUUID().toString(), grData);
                future.whenComplete((result, ex) -> {
                    if (ex == null) {
                    	log.info("Sent message with offset=[" + result.getRecordMetadata().offset() 
                                + "] and partition=[" + result.getRecordMetadata().partition()
                                + "] and message=[" + grData
                                + "]");
                    } else {
                    	 log.info("Unable to send message=[" +
                    			 grData + "] due to : " + ex.getMessage());
                    }
                });
        }
        
        
        public void sendDataOnTopicStackEnbleSC(ShpmntConfInData shpmntConfInData){
    		CompletableFuture<SendResult<String, ShpmntConfInData>> future = templateSCSchema.send(topicStackEnbleSC, UUID.randomUUID().toString(), shpmntConfInData);
    		future.whenComplete((result, ex) -> {
                if (ex == null) {
                	log.info("Sent message with offset=[" + result.getRecordMetadata().offset() 
                            + "] and partition=[" + result.getRecordMetadata().partition()
                            + "] and message=[" + shpmntConfInData
                            + "]");
                } else {
                	 log.info("Unable to send message=[" +
                			 shpmntConfInData + "] due to : " + ex.getMessage());
                }
            });
    }

    public void sendDataOnTopicStackEnbleAdj(ProductAdjustmentDto adjData, String flowRefNum) {
        CompletableFuture<SendResult<String, ProductAdjustmentDto>> future = templateAdj.send(topicStackEnbleAdj, flowRefNum, adjData);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                String remarks = "Sent message with offset=[" + result.getRecordMetadata().offset()
                        + "] and partition=[" + result.getRecordMetadata().partition()
                        + "] and message=[" + adjData
                        + "]";

                log.info(remarks);
                mAuditDataService.insertAuditData(remarks,flowRefNum,FAILED,TOPIC);
            } else {
                log.info("Unable to send message=[" +
                        adjData + "] due to : " + ex.getMessage());
            }
        });
    }
    public void sendDataOnTopicStackEnbleOrder(InwardRootStackEnable orderPayload, String flowRefNum){
        CompletableFuture<SendResult<String, com.integrations.orderprocessing.model.req_body.stackenable.InwardRootStackEnable>> future = orderTemplate.send(topicStackEnbleOrder, flowRefNum,orderPayload);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                String remarks ="Sent message with offset=[" + result.getRecordMetadata().offset()
                        + "] and partition=[" + result.getRecordMetadata().partition()
                        + "] and message=[" + orderPayload
                        + "]";
                log.info(remarks);
                mAuditDataService.insertAuditData(remarks,flowRefNum,FAILED,TOPIC);
            } else {
                log.info("Unable to send message=[" +
                        orderPayload + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendDataOnTopicFleetEnableOrgSetup(FEOrgData feOrgData, String flowRefNum) {
        CompletableFuture<SendResult<String, FEOrgData>> future = templateOrgSetup.send(topicFleetEnableOrgSetup, flowRefNum, feOrgData);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                String remarks = "Sent message with offset=[" + result.getRecordMetadata().offset()
                        + "] and partition=[" + result.getRecordMetadata().partition()
                        + "] and message=[" + feOrgData
                        + "]" ;

                log.info(remarks);
                mAuditDataService.insertAuditData(remarks,flowRefNum,FAILED, TOPIC);
            } else {
                log.info("Unable to send message=[" +
                        feOrgData + "] due to : " + ex.getMessage());
            }
        });
    }


    public void sendDataOnTopicFeOrder(InwardRootStackEnable reqOrderData, String reqType, String flowRefNum) {
        CompletableFuture<SendResult<String , InwardRootStackEnable>> future = orderTemplate.send(topicFleetEnableOrder,flowRefNum,reqOrderData);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                String remarks = "Sent message with offset=[" + result.getRecordMetadata().offset()
                        + "] and partition=[" + result.getRecordMetadata().partition()
                        + " Request type "+ reqType
                        + "] and message=[" + reqOrderData
                        + "]" ;

                log.info(remarks);
                mAuditDataService.insertAuditData(remarks,flowRefNum,FAILED,TOPIC);
            } else {
                log.info("Unable to send message=[" +
                        reqOrderData + "] due to : " + ex.getMessage());
            }
        });
    }
    public void sendDataOnTopicFleetEnableAccountSetup(FEAccountCreatedEvent accountCreatedEvent, String flowRefNum) {
        CompletableFuture<SendResult<String, FEAccountCreatedEvent>> future = templateAccountSetup.send(topicFleetEnableAccountCreate, flowRefNum, accountCreatedEvent);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                String remarks = "Sent message with offset=[" + result.getRecordMetadata().offset()
                        + "] and partition=[" + result.getRecordMetadata().partition()
                        + "] and message=[" + accountCreatedEvent
                        + "]";
                log.info(remarks);
                mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, TOPIC);
            } else {
                log.info("Unable to send message=[" +
                        accountCreatedEvent + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendDataOnTopicFleetEnableAccountDelete(FEAccountDeleteEvent feAccountDeleteEvent, String flowRefNum) {
        CompletableFuture<SendResult<String, FEAccountDeleteEvent>> future = templateAccountDelete.send(topicFleetEnableAccountDelete, flowRefNum, feAccountDeleteEvent);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                String remarks = "Sent message with offset=[" + result.getRecordMetadata().offset()
                        + "] and partition=[" + result.getRecordMetadata().partition()
                        + "] and message=[" + feAccountDeleteEvent
                        + "]";

                log.info(remarks);
                mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, TOPIC);

            } else {
                log.info("Unable to send message=[" +
                        feAccountDeleteEvent + "] due to : " + ex.getMessage());
            }
        });

    }

    public void sendDataOnTopicFleetEnableOrderDelete(FEOrderDeletedEvent feOrderDeletedEvent, String flowRefNum) {
        CompletableFuture<SendResult<String, FEOrderDeletedEvent>> future = templateOrderDelete.send(topicFleetEnableOrderDelete, flowRefNum, feOrderDeletedEvent);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                String remarks = "Sent message with offset=[" + result.getRecordMetadata().offset()
                        + "] and partition=[" + result.getRecordMetadata().partition()
                        + "] and message=[" + feOrderDeletedEvent
                        + "]";
                log.info(remarks);
                mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, TOPIC);
            } else {
                log.info("Unable to send message=[" +
                        feOrderDeletedEvent + "] due to : " + ex.getMessage());
            }
        });

    }

    public void sendDataOnTopicStackEnableFEOrderSave(FEOrderSave feOrderSave, String flowRefNum) {
        CompletableFuture<SendResult<String, FEOrderSave>> future = templateFEOrderSave.send(topicStackEnableOrderSave, flowRefNum, feOrderSave);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                String remarks = "Sent message with offset=[" + result.getRecordMetadata().offset()
                        + "] and partition=[" + result.getRecordMetadata().partition()
                        + "] and message=[" + feOrderSave
                        + "]";

                log.info(remarks);
                mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, TOPIC);
            } else {
                log.info("Unable to send message=[" +
                        feOrderSave + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendDataOnTopicWMSData(FEwmsDataSave fEwmsDataSave, String flowRefNum) {

        CompletableFuture<SendResult<String,FEwmsDataSave >> future = templateWmsData.send(topicWMSData, flowRefNum, fEwmsDataSave);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
               String remarks = "Sent message with offset=[" + result.getRecordMetadata().offset()
                        + "] and partition=[" + result.getRecordMetadata().partition()
                        + "] and message=[" + fEwmsDataSave
                        + "]";
                log.info(remarks);

                mAuditDataService.insertAuditData(remarks, flowRefNum, FAILED, TOPIC);
            } else {
                log.info("Unable to send message=[" +
                        fEwmsDataSave + "] due to : " + ex.getMessage());
            }
        });

    }
}

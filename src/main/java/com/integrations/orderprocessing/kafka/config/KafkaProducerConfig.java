package com.integrations.orderprocessing.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;


@Configuration
public class KafkaProducerConfig {

    @Value("${app.kafka.topic.stackenable-gr}")
    private String topicStackEnbleGR;
    
    @Value("${app.kafka.topic.stackenable-sc}")
    private String topicStackEnbleSC;

    @Bean
    NewTopic createStackEnableGRTopic(){
    	return TopicBuilder
    			.name(topicStackEnbleGR)
    			.partitions(3)
    			.replicas(1)
                .build();
    }
    
    @Bean
    NewTopic createStackEnableSCTopic(){
    	return TopicBuilder
    			.name(topicStackEnbleSC)
    			.partitions(3)
    			.replicas(1)
                .build();
    }
}

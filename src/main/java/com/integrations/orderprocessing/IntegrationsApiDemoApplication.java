package com.integrations.orderprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class IntegrationsApiDemoApplication {

	public static void main(String[] args) {
		log.info("It is working...");;
		SpringApplication.run(IntegrationsApiDemoApplication.class, args);
	}

}

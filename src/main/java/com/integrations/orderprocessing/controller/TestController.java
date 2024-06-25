package com.integrations.orderprocessing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${app.context.path}")
public class TestController {
	
	@GetMapping("/healthz")
	public String getMessage() {
//		log.info("TestController getMessage() called");
		return "Health is good!!!";
	}
}

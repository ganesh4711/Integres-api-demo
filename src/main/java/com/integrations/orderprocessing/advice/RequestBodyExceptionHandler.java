package com.integrations.orderprocessing.advice;

import static com.integrations.orderprocessing.constants.HttpConstants.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.integrations.orderprocessing.dto.ResponseDTO;

//@RestControllerAdvice
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestBodyExceptionHandler {
	
	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
		StringBuffer sb = new StringBuffer();
		
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});
		
		errors.entrySet().stream().forEach((entry)-> sb.append(entry.getValue())
			.append(" & "));
		
		String remarks = sb.toString();
		remarks = remarks.substring(0, remarks.length()-3);
		
		ResponseDTO response = new ResponseDTO();
		response.setMessage(remarks);
		response.setCode(FAILURE_CODE);
		response.setSuccess(false);
		response.setRefNum("-1");
        
        return ResponseEntity.badRequest().body(response);
    }
}

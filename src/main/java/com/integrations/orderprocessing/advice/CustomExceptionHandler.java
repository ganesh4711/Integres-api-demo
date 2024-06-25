package com.integrations.orderprocessing.advice;

import static com.integrations.orderprocessing.constants.HttpConstants.FAILURE_CODE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.integrations.orderprocessing.dto.FEResponseDTO;
import com.integrations.orderprocessing.dto.ResponseDTO;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleSecurityException(Exception ex) {
        ResponseEntity<?> retResponse = null;

        if (ex instanceof BadCredentialsException) {
            retResponse = ResponseEntity.status(401).body(new FEResponseDTO("failed", "Authentication Failure"));
        } else if (ex instanceof AccessDeniedException) {
            retResponse = ResponseEntity.status(403).body(new FEResponseDTO("failed", "Not Authorized"));
        } else if (ex instanceof SignatureException || ex instanceof MalformedJwtException) {
            retResponse = ResponseEntity.status(403).body(new FEResponseDTO("failed", "JWT Signature not valid"));
        } else if (ex instanceof ExpiredJwtException) {
            retResponse = ResponseEntity.status(403).body(new FEResponseDTO("failed", "JWT Token already expired!"));
        } else if (ex instanceof HttpMessageNotReadableException) {
            retResponse = ResponseEntity.status(400).body(new FEResponseDTO("failed", "Bad Request"));
        } else if (ex instanceof MethodArgumentNotValidException) {

            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            String username = authentication.getName();

            log.info("$$$$$$$ UserName:: {}", username);

            if (username.equals("anonymousUser")) {
                String remarks =getResponseDTO((MethodArgumentNotValidException) ex);

                		ResponseDTO response = new ResponseDTO();
                		response.setMessage(remarks);
                		response.setCode(FAILURE_CODE);
                		response.setSuccess(false);
                		response.setRefNum("-1");


                retResponse = ResponseEntity.badRequest().body(response);
            } else {
                String remarks = getResponseDTO((MethodArgumentNotValidException) ex);
                retResponse = ResponseEntity.status(400).body(new FEResponseDTO("failed",remarks));
            }
        } else {
            retResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FEResponseDTO("failed", "Unknow Reason:: " + ex.getMessage()));
        }


        return retResponse;
    }

    private String getResponseDTO(MethodArgumentNotValidException ex) {
        MethodArgumentNotValidException ex1 = ex;
        StringBuffer sb = new StringBuffer();

        Map<String, String> errors = new HashMap<>();
        ex1.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        errors.entrySet().stream().forEach((entry) -> sb.append(entry.getValue())
                .append(" & "));

        String remarks = sb.toString();
        remarks = remarks.substring(0, remarks.length() - 3);

        return remarks;

    }

}

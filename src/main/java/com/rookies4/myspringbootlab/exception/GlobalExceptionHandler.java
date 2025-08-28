package com.rookies4.myspringbootlab.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // BusinessException 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", ex.getHttpStatus().value());
        errorResponse.put("message", ex.getMessage());   // ISBN 중복 같은 메시지
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(errorResponse);
    }

    // 그 외 예외 처리 (안정성용)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", 500);
        errorResponse.put("message", ex.getMessage() != null ? ex.getMessage() : "Internal Server Error");
        return ResponseEntity
                .status(500)
                .body(errorResponse);
    }
}

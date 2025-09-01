package com.rookies4.myspringbootlab.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 기존에 RESOURCE_NOT_FOUND 처리 등이 있다면 그대로 두고 추가
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getErrorCode().getMessage());
        body.put("details", ex.getDetails()); // 필요시 추가
        return new ResponseEntity<>(body, ex.getErrorCode().getStatus());
    }

    // 다른 예외 처리도 여기에 추가 가능
}

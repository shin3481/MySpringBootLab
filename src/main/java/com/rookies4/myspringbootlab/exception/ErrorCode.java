package com.rookies4.myspringbootlab.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common errors - 공통으로 사용할 수 있는 일반적인 에러 코드
    RESOURCE_NOT_FOUND("%s not found with %s: %s", HttpStatus.NOT_FOUND),

    // Book specific errors - 도서 관련 특수한 경우
    ISBN_DUPLICATE("Book already exists with ISBN: %s", HttpStatus.CONFLICT);

    private final String messageTemplate;
    private final HttpStatus httpStatus;

    public String formatMessage(Object... args) {
        return String.format(messageTemplate, args);
    }
}
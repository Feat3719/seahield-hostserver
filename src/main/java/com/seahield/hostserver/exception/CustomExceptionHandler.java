package com.seahield.hostserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    // 실패 메시지
    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<?> notFound(ErrorException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse(exception.getMessage()));
    }

    // 성공 메시지
    @ExceptionHandler(SuccessException.class)
    public ResponseEntity<?> success(SuccessException exception) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse(exception.getMessage()));
    }

}

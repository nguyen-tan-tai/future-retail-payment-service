package com.example.payment.exception;

import com.example.payment.dto.response.ErrorResponse;
import java.time.Instant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handle(ValidationException ex) {
        // construct ErrorResponse record directly
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(Instant.now(), 500, null, ex.getMessage(), ex.getErrors()));
    }
}

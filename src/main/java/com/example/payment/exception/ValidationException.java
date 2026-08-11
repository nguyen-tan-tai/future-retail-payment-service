package com.example.payment.exception;

import com.example.payment.dto.response.ErrorDetail;
import java.util.List;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -2528616063865346804L;
    private final List<ErrorDetail> errors;

    public ValidationException(List<ErrorDetail> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public List<ErrorDetail> getErrors() {
        return errors;
    }
}
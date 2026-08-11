package com.example.payment.validator;

import com.example.payment.dto.response.ErrorDetail;
import com.example.payment.exception.ValidationException;
import java.util.List;

public abstract class BaseValidator {

    public void throwIfErrors(List<ErrorDetail> errors) {
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}

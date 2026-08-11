package com.example.payment.validator;

import com.example.payment.dto.request.PaymentCreateRequest;
import com.example.payment.dto.request.PaymentSuccessRequest;
import com.example.payment.dto.response.ErrorDetail;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaymentValidator extends BaseValidator {

    public List<ErrorDetail> validate(PaymentCreateRequest request) {
        return List.of();
    }

    public void validateOrThrow(PaymentCreateRequest request) {
        throwIfErrors(validate(request));
    }

    public List<ErrorDetail> validate(String paymentId, PaymentSuccessRequest request) {
        if (paymentId == null || paymentId.isBlank()) {
            return List.of(new ErrorDetail("paymentId", "Payment ID is required"));
        }
        return List.of();
    }

    public void validateOrThrow(String paymentId, PaymentSuccessRequest request) {
        throwIfErrors(validate(paymentId, request));
    }
}

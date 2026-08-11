package com.example.payment.dto.request;

import com.example.payment.model.PaymentMethod;
import jakarta.validation.constraints.NotBlank;

public class PaymentSuccessRequest {

    @NotBlank
    private String gatewayTransactionId;

    @NotBlank
    private String paymentMethod;

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethodAsEnum() {
        return PaymentMethod.valueOf(paymentMethod.toUpperCase());
    }
}

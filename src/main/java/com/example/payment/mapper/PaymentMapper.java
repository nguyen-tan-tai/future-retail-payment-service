package com.example.payment.mapper;

import com.example.payment.model.Payment;
import com.example.schema.payment.PaymentEvent;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentEvent toPaymentEvent(Payment payment, String ipAddress, String userAgent) {
        return PaymentEvent.newBuilder()
                .setPaymentId(payment.getId().toString())
                .setUserId(payment.getUserId())
                .setAmount(payment.getAmount().doubleValue())
                .setIpAddress(ipAddress)
                .setUserAgent(userAgent)
                .setEventTime(payment.getCreatedAt().toEpochMilli())
                .build();
    }
}

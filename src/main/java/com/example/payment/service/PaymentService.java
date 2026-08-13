package com.example.payment.service;

import com.example.payment.context.RequestContext;
import com.example.payment.dto.request.PaymentCreateRequest;
import com.example.payment.dto.request.PaymentSuccessRequest;
import com.example.payment.mapper.PaymentMapper;
import com.example.payment.model.Payment;
import com.example.payment.model.PaymentStatus;
import com.example.payment.producer.PaymentEventProducer;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.validator.PaymentValidator;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentValidator paymentValidator;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentEventProducer paymentEventProducer;

    public PaymentService(PaymentValidator paymentValidator, PaymentRepository paymentRepository,
            PaymentMapper paymentMapper,
            PaymentEventProducer transactionEventProducer) {
        this.paymentValidator = paymentValidator;
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.paymentEventProducer = transactionEventProducer;
    }

    /**
     * TODO: Replace direct Kafka publish with Transactional Outbox. Current flow: REST -> PostgreSQL -> Kafka Problem: - PostgreSQL commit succeeds but Kafka publish fails => event lost. - Kafka
     * publish succeeds but DB transaction rolls back => inconsistent state. Future solution: - Write business data + outbox record in the same DB transaction. - Use Outbox Poller or Debezium CDC to
     * publish to Kafka. - Mark outbox record as published after successful send.
     */
    public Payment createPayment(PaymentCreateRequest request, RequestContext context) {
        paymentValidator.validateOrThrow(request);
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setOrderId(request.getOrderId());
        payment.setMerchantId(request.getMerchantId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(Instant.now());
        Payment newPayment = paymentRepository.save(payment);
        paymentEventProducer.publish(paymentMapper.toPaymentEvent(newPayment, context.ipAddress(), context.userAgent()));
        log.info("Payment created with ID: {} and request: {}", newPayment.getId(), request);
        return newPayment;
    }

    public Payment confirmPayment(String paymentId, PaymentSuccessRequest request, RequestContext context) {
        paymentValidator.validateOrThrow(paymentId, request);
        Payment payment = paymentRepository.findById(UUID.fromString(paymentId)).get();
        payment.setCompletedAt(Instant.now());
        payment.setStatus(PaymentStatus.SUCCEEDED);
        payment.setGatewayTransactionId(request.getGatewayTransactionId());
        payment.setPaymentMethod(request.getPaymentMethodAsEnum());
        paymentRepository.save(payment);
        paymentEventProducer.publish(paymentMapper.toPaymentEvent(payment, context.ipAddress(), context.userAgent()));
        log.info("Payment confirmed with ID: {} and request: {}", paymentId, request);
        return payment;
    }

}

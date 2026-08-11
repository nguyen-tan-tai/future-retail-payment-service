package com.example.payment.controller;

import com.example.payment.context.RequestContext;
import com.example.payment.context.RequestContextProvider;
import com.example.payment.dto.request.PaymentCreateRequest;
import com.example.payment.dto.request.PaymentSuccessRequest;
import com.example.payment.model.Payment;
import com.example.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final RequestContextProvider requestContextProvider;
    private final PaymentService paymentService;

    public PaymentController(RequestContextProvider requestContextProvider, PaymentService paymentService) {
        this.requestContextProvider = requestContextProvider;
        this.paymentService = paymentService;
    }

    @PostMapping
    public Payment createPayment(@Valid @RequestBody PaymentCreateRequest request) {
        RequestContext context = requestContextProvider.get();
        log.info("Creating payment with request: {}", request);
        return paymentService.createPayment(request, context);
    }

    @PostMapping("/{paymentId}/callback/success")
    public Payment successPayment(@PathVariable String paymentId, @Valid @RequestBody PaymentSuccessRequest request) {
        log.info("Confirming payment with ID: {} and request: {}", paymentId, request);
        RequestContext context = requestContextProvider.get();
        return paymentService.confirmPayment(paymentId, request, context);
    }
}

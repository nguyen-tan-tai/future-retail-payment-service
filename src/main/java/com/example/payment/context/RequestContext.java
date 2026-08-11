package com.example.payment.context;

public record RequestContext(
        String ipAddress,
        String userAgent,
        String requestId,
        String correlationId) {
}

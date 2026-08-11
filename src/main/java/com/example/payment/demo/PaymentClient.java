package com.example.payment.demo;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PaymentClient {

    private static final List<String> MERCHANTS = List.of(
            "AMAZON",
            "APPLE",
            "EBAY",
            "NETFLIX",
            "SPOTIFY");
    private static final List<String> userId = List.of(
            "282a5208-020d-4593-a33a-b0732a92d2c3",
            "7b2756a6-c6a5-4cf5-b16e-9f6ee1a52095",
            "af7f2824-4fb1-48b2-bc69-4511ec7ad9fb",
            "c825a63a-c2a0-4acb-b0e2-f80b9a4e7262",
            "78325d7e-b852-4583-aa62-b53fdc304912",
            "dcae869c-515f-4cae-8232-224bd37aead5",
            "eb250aa8-f789-44ec-b76e-bfd767971f1e",
            "c73fec0d-8c24-4f32-9de3-16e5d07fd3d6",
            "4d21577b-93c1-476d-a9ca-88ae74d1c781",
            "f74630f3-0c64-4fb7-bb17-40654183e420");

    private final RestClient restClient;

    public PaymentClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8085")
                .build();
    }

    public PaymentResponse createPayment() {

        CreatePaymentRequest request = new CreatePaymentRequest(
                randomUserId(),
                UUID.randomUUID().toString(),
                randomMerchant(),
                BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10, 1000)),
                "USD");

        return restClient.post()
                .uri("/api/v1/payments")
                .body(request)
                .retrieve()
                .body(PaymentResponse.class);
    }

    public void completePayment(String paymentId) {

        CompletePaymentRequest request = new CompletePaymentRequest(
                "GW-" + UUID.randomUUID(),
                "CREDIT_CARD");
        try {
            restClient.post()
                    .uri("/api/v1/payments/{id}/callback/success", paymentId)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String randomMerchant() {
        return MERCHANTS.get(
                ThreadLocalRandom.current().nextInt(MERCHANTS.size()));
    }

    private String randomUserId() {
        int userIdOrder = ThreadLocalRandom.current().nextInt(userId.size() + 10);// 20
        if (userIdOrder >= userId.size()) {
            return UUID.randomUUID().toString();
        }
        return userId.get(userIdOrder);
    }

    public record CreatePaymentRequest(
            String userId,
            String orderId,
            String merchantId,
            BigDecimal amount,
            String currency) {
    }

    public record CompletePaymentRequest(
            String gatewayTransactionId,
            String paymentMethod) {
    }

    public record PaymentResponse(
            String id,
            String userId,
            String orderId,
            String merchantId,
            BigDecimal amount,
            String currency,
            String status) {
    }
}

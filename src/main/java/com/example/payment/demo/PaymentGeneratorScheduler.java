package com.example.payment.demo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentGeneratorScheduler {
    private final PaymentClient paymentClient;
    private final Deque<PaymentClient.PaymentResponse> pendingPayments = new ArrayDeque<>();

    public PaymentGeneratorScheduler(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @Scheduled(fixedDelay = 1000)
    public void generatePayments() {
        generate();
    }

    public void generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        // 60% tạo payment mới
        if (random.nextInt(100) < 60) {
            PaymentClient.PaymentResponse payment = paymentClient.createPayment();
            pendingPayments.addLast(payment);
            System.out.printf(
                    "[CREATE] %s amount=%s merchant=%s%n",
                    payment.id(),
                    payment.amount(),
                    payment.merchantId());
        }
        if (pendingPayments.isEmpty()) {
            return;
        }
        int action = random.nextInt(100);
        PaymentClient.PaymentResponse payment = pendingPayments.peekFirst();
        // 80% callback thành công
        if (action < 80) {
            paymentClient.completePayment(payment.id());
            pendingPayments.removeFirst();
            System.out.printf("[SUCCESS] %s%n", payment.id());
        }
        // 10% duplicate callback
        else if (action < 90) {
            paymentClient.completePayment(payment.id());
            paymentClient.completePayment(payment.id());
            pendingPayments.removeFirst();
            System.out.printf("[DUPLICATE] %s%n", payment.id());
        }
        // 10% giữ nguyên PENDING
        else {
            System.out.printf("[PENDING] %s%n", payment.id());
        }
    }
}

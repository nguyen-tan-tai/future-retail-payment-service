package com.example.payment.producer;

import com.example.payment.config.KafkaProperties;
import com.futureretail.schema.payment.PaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventProducer {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate,
            KafkaProperties kafkaProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProperties = kafkaProperties;
    }

    public void publish(PaymentEvent event) {
        kafkaTemplate.send(kafkaProperties.topics().paymentEvents(), event.getPaymentId(), event);
    }
}

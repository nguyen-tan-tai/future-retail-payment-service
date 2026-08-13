-- Insert sample payments
INSERT INTO payments (id, payment_id, order_id, user_id, amount, currency, status, payment_method, reference_number, created_at, updated_at) VALUES
(1, 'PAY-2026-08-001', 1, 1, 349.95, 'USD', 'COMPLETED', 'CREDIT_CARD', 'REF-CC-001', '2026-08-10 10:30:00', '2026-08-10 10:35:00'),
(2, 'PAY-2026-08-002', 2, 2, 129.99, 'USD', 'COMPLETED', 'CREDIT_CARD', 'REF-CC-002', '2026-08-10 11:15:00', '2026-08-10 11:20:00'),
(3, 'PAY-2026-08-003', 3, 3, 249.97, 'USD', 'PENDING', 'CREDIT_CARD', 'REF-CC-003', '2026-08-11 14:20:00', '2026-08-11 14:20:00'),
(4, 'PAY-2026-08-004', 4, 4, 89.99, 'USD', 'COMPLETED', 'PAYPAL', 'REF-PP-001', '2026-08-09 08:00:00', '2026-08-09 08:05:00'),
(5, 'PAY-2026-08-005', 5, 5, 199.98, 'USD', 'FAILED', 'CREDIT_CARD', 'REF-CC-004', '2026-08-08 13:45:00', '2026-08-08 13:50:00');

-- Insert sample payment transactions
INSERT INTO payment_transactions (payment_id, transaction_type, amount, status, gateway_response) VALUES
(1, 'AUTHORIZATION', 349.95, 'SUCCESS', '{"gateway":"stripe","auth_code":"AUTH123456","timestamp":"2026-08-10T10:30:30Z"}'),
(1, 'CAPTURE', 349.95, 'SUCCESS', '{"gateway":"stripe","capture_id":"CAP123456","timestamp":"2026-08-10T10:35:00Z"}'),
(2, 'AUTHORIZATION', 129.99, 'SUCCESS', '{"gateway":"stripe","auth_code":"AUTH123457","timestamp":"2026-08-10T11:15:30Z"}'),
(2, 'CAPTURE', 129.99, 'SUCCESS', '{"gateway":"stripe","capture_id":"CAP123457","timestamp":"2026-08-10T11:20:00Z"}'),
(3, 'AUTHORIZATION', 249.97, 'PENDING', '{"gateway":"stripe","auth_code":"AUTH123458","timestamp":"2026-08-11T14:20:30Z"}'),
(4, 'AUTHORIZATION', 89.99, 'SUCCESS', '{"gateway":"paypal","auth_code":"AUTH123459","timestamp":"2026-08-09T08:00:30Z"}'),
(4, 'CAPTURE', 89.99, 'SUCCESS', '{"gateway":"paypal","capture_id":"CAP123459","timestamp":"2026-08-09T08:05:00Z"}'),
(5, 'AUTHORIZATION', 199.98, 'FAILED', '{"gateway":"stripe","error_code":"card_declined","error_message":"Card declined","timestamp":"2026-08-08T13:45:30Z"}');

-- Insert sample risk alerts
INSERT INTO risk_alerts (payment_id, alert_type, risk_level, reason, is_resolved) VALUES
(3, 'HIGH_AMOUNT', 'MEDIUM', 'Transaction amount exceeds normal spending pattern', FALSE),
(5, 'DECLINED_CARD', 'HIGH', 'Multiple failed card attempts detected', FALSE);

-- Insert sample payment refunds
INSERT INTO payment_refunds (id, refund_id, payment_id, refund_amount, reason, status) VALUES
(1, 'REF-2026-08-001', 1, 349.95, 'Order cancelled by customer', 'COMPLETED');

-- Insert sample payment outbox events
INSERT INTO payment_outbox (aggregate_id, aggregate_type, event_type, payload, processed) VALUES
(1, 'Payment', 'PAYMENT_AUTHORIZED', '{"payment_id":1,"amount":349.95,"user_id":1,"timestamp":"2026-08-10T10:30:30Z"}', TRUE),
(1, 'Payment', 'PAYMENT_CAPTURED', '{"payment_id":1,"amount":349.95,"timestamp":"2026-08-10T10:35:00Z"}', TRUE),
(2, 'Payment', 'PAYMENT_AUTHORIZED', '{"payment_id":2,"amount":129.99,"user_id":2,"timestamp":"2026-08-10T11:15:30Z"}', TRUE),
(2, 'Payment', 'PAYMENT_CAPTURED', '{"payment_id":2,"amount":129.99,"timestamp":"2026-08-10T11:20:00Z"}', TRUE),
(3, 'Payment', 'PAYMENT_AUTHORIZED', '{"payment_id":3,"amount":249.97,"user_id":3,"timestamp":"2026-08-11T14:20:30Z"}', FALSE),
(4, 'Payment', 'PAYMENT_AUTHORIZED', '{"payment_id":4,"amount":89.99,"user_id":4,"timestamp":"2026-08-09T08:00:30Z"}', TRUE),
(4, 'Payment', 'PAYMENT_CAPTURED', '{"payment_id":4,"amount":89.99,"timestamp":"2026-08-09T08:05:00Z"}', TRUE),
(5, 'Payment', 'PAYMENT_FAILED', '{"payment_id":5,"amount":199.98,"user_id":5,"error":"card_declined","timestamp":"2026-08-08T13:45:30Z"}', TRUE),
(1, 'Payment', 'PAYMENT_REFUNDED', '{"refund_id":"REF-2026-08-001","payment_id":1,"refund_amount":349.95,"timestamp":"2026-08-10T15:00:00Z"}', TRUE);

package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.PaymentIntentDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentIntentDto> createPaymentIntent(@RequestBody PaymentIntentDto paymentIntentDto)
            throws StripeException {
        // Define your payment intent parameters
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(5000L) // amount in cents (e.g., $50.00)
                .setCurrency("usd")
                .build();

        // Create a PaymentIntent with the order amount and currency
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return ResponseEntity.ok(new PaymentIntentDto(
                paymentIntent.getId(),
                paymentIntent.getClientSecret(),
                paymentIntentDto.userId()));
    }
}

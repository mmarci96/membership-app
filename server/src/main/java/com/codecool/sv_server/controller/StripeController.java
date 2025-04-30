package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.MembershipStatusDto;
import com.codecool.sv_server.dto.PaymentIntentDto;
import com.codecool.sv_server.dto.SubscriptionReqDto;
import com.codecool.sv_server.exception.ApiException;
import com.codecool.sv_server.service.MembershipService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private final MembershipService membershipService;

    @Autowired
    public StripeController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

   
@PostMapping("/create-payment-intent")
public ResponseEntity<PaymentIntentDto> createPaymentIntent(
        @RequestBody PaymentIntentDto paymentIntentDto) {
    try {
        var params = PaymentIntentCreateParams.builder().setAmount(50L).setCurrency("usd").build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return ResponseEntity.ok(
                new PaymentIntentDto(
                        paymentIntent.getId(),
                        paymentIntent.getClientSecret(),
                        paymentIntentDto.userId()));
    } catch (StripeException | RuntimeException e) {
        throw new ApiException("Stripe error: " + e.getMessage(), 500);
    }
}

    @PostMapping("/payment-status")
    public ResponseEntity<MembershipStatusDto> createMembershipStatus(
            @RequestBody SubscriptionReqDto subscriptionReqDto) {
        var status = membershipService.startMembership(subscriptionReqDto);
        return ResponseEntity.ok(status);
    }
}

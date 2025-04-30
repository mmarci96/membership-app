package com.codecool.sv_server.dto;

public record PaymentIntentDto(String stripePaymentIntentId, String clientSecret, long userId) {}

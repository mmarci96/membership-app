package com.codecool.sv_server.dto;

public record SubscriptionReqDto(long userId, boolean paymentStatus,
                                 String paymentIntent) {
}

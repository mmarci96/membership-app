package com.codecool.sv_server.dto;

import com.codecool.sv_server.entity.SubscriptionStatus;

import java.time.LocalDate;

public record MembershipStatusDto(long userId, SubscriptionStatus status,
                                  LocalDate endDate) {
}

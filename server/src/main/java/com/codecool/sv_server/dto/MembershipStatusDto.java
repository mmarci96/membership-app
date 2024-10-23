package com.codecool.sv_server.dto;

import java.time.LocalDate;

public record MembershipStatusDto(String email, boolean accepted,
                                  LocalDate endDate) {
}

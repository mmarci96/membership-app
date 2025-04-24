package com.codecool.sv_server.dto;

import com.codecool.sv_server.entity.Role;

public record TokenInfo(String email, Long userId, Role role) {
}

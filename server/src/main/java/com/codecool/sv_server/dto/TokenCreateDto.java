package com.codecool.sv_server.dto;

import com.codecool.sv_server.entity.Role;

public record TokenCreateDto(Long id, Role role) {
}

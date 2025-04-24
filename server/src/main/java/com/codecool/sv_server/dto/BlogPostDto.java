package com.codecool.sv_server.dto;

import java.time.LocalDateTime;

public record BlogPostDto(long id, String title, String content,
        LocalDateTime createdAt) {
}

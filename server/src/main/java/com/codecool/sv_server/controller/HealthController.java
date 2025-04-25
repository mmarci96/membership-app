package com.codecool.sv_server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codecool.sv_server.dto.HealthResponse;

@RestController
@RequestMapping("/")
public class HealthController {

    @GetMapping("/hello")
    ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, World!");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("PONG");
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("OK"));
    }
}

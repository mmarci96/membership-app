package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.UserDetailsDto;

import com.codecool.sv_server.service.UserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserDetailsService userDetailsService;
    public UserController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @GetMapping("/account")
    public ResponseEntity<UserDetailsDto> getUserDetails(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        var res = userDetailsService.getUserDetails(email);
        if (res == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(res);
    }
    @PostMapping("/account")
    public ResponseEntity<UserDetailsDto> createUserDetails(@RequestBody UserDetailsDto userDetailsDto) {
        return ResponseEntity.ok(userDetailsService.setupUserDetails(userDetailsDto));
    }

}

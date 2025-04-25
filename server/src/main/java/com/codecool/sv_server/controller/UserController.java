package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.UserDetailsDto;

import com.codecool.sv_server.service.UserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserDetailsService userDetailsService;

    public UserController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable long id) {
        var res = userDetailsService.getUserDetailsByUserId(id);
        if (res == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(res);
    }

    @PostMapping("/account")
    public ResponseEntity<UserDetailsDto> createUserDetails(@RequestBody UserDetailsDto userDetailsDto) {
        return ResponseEntity.ok(userDetailsService.setupUserDetails(userDetailsDto));
    }

}

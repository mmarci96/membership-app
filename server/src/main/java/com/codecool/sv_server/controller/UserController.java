package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.UserDetailsDto;
import com.codecool.sv_server.exception.ApiException;
import com.codecool.sv_server.service.UserDetailsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserDetailsService userDetailsService;

    public UserController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<UserDetailsDto> getUserDetails(
            @AuthenticationPrincipal Jwt jwt, @PathVariable long id) {
        long userId = jwt.getClaim("userId");
        if (id != userId) {
            throw new ApiException("Unauthorized request!", 403);
        }
        var res = userDetailsService.getUserDetailsByUserId(id);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/account")
    public ResponseEntity<UserDetailsDto> createUserDetails(
            @RequestBody UserDetailsDto userDetailsDto) {
        return ResponseEntity.ok(userDetailsService.createUserDetails(userDetailsDto));
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<String> deleteUserDetails(
            @AuthenticationPrincipal Jwt jwt, @RequestBody UserDetailsDto userDetailsDto) {
        long id = userDetailsDto.userId();
        long userId = jwt.getClaim("userId");
        if (id != userId) {
            throw new ApiException("Unauthorized request!", 403);
        }
        userDetailsService.deleteUser(id);
        return ResponseEntity.ok("User details deleted succesfully!");
    }

    @PatchMapping("/account")
    public ResponseEntity<UserDetailsDto> updateUserDetails(
            @AuthenticationPrincipal Jwt jwt, @RequestBody UserDetailsDto userDetailsDto) {
        long id = userDetailsDto.userId();
        long userId = jwt.getClaim("userId");
        if (id != userId) {
            throw new ApiException("Unauthorized request!", 403);
        }
        var result = userDetailsService.updateUserDetails(userDetailsDto, id);
        return ResponseEntity.ok(result);
    }
}

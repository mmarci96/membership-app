package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.SignupResDto;
import com.codecool.sv_server.dto.UserSignupDto;
import com.codecool.sv_server.entity.User;
import com.codecool.sv_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResDto> signup(
            @RequestBody UserSignupDto userSignupDto) {
        System.out.println(userSignupDto.email() + " " + userSignupDto.password());
        boolean success = userService.signup(userSignupDto);
        return ResponseEntity.ok(new SignupResDto(userSignupDto.email()));
    }
}

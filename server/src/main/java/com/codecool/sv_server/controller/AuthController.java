package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.SignupResponseDto;
import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SignupResponseDto> signup(
            @RequestBody SignupRequestDto signupRequestDto) {
        System.out.println(signupRequestDto.email() + " " + signupRequestDto.password());
        long id = userService.signup(signupRequestDto);
        return ResponseEntity.ok(new SignupResponseDto(signupRequestDto.email(), id));
    }
}

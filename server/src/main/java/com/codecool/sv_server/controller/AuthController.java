package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.LoginRequestDto;
import com.codecool.sv_server.dto.LoginResponseDto;
import com.codecool.sv_server.dto.SignupResponseDto;
import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService userService;

    @Autowired
    public AuthController(AuthService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        var loginResponseDto = userService.authenticateUser(loginRequestDto);
        if (loginResponseDto == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new LoginResponseDto(loginResponseDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(
            @RequestBody SignupRequestDto signupRequestDto) {
        var res = userService.createUser(signupRequestDto);
        if (res == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res);
    }
    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token,
                                                  @RequestParam("userId") Long userId) {
        boolean isActivated = userService.activateUserAccount(userId, token);
        if (isActivated) {
            return ResponseEntity.ok("Account activated successfully!");
        } else {
            return ResponseEntity.badRequest().body("Activation link is invalid or expired.");
        }
    }
}

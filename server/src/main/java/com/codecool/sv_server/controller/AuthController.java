package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.SignupResponseDto;
import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(
            @RequestBody SignupRequestDto signupRequestDto) {
        var res = userService.signup(signupRequestDto);
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

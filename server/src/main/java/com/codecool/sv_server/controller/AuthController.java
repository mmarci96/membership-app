package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.LoginRequestDto;
import com.codecool.sv_server.dto.LoginResponseDto;
import com.codecool.sv_server.dto.TokenCreateDto;
import com.codecool.sv_server.dto.VerifyCodeRequestDto;
import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.service.AuthService;
import com.codecool.sv_server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService userService;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginReqController(@RequestBody LoginRequestDto loginRequestDto) {
        TokenCreateDto data = userService.validateLogin(loginRequestDto);
        if (data == null) {
            return ResponseEntity.status(401).body("Invalid email or password!");
        }

        String token = tokenService.generateToken(loginRequestDto.email(),
                data.role(), data.id());
        return ResponseEntity.ok(new LoginResponseDto(token, data.id()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody SignupRequestDto signupRequestDto) {
        var res = userService.registerUser(signupRequestDto);
        if (res == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateAccount(
            @RequestBody VerifyCodeRequestDto verifyCodeRequestDto,
            @AuthenticationPrincipal Jwt jwt) {
        var userId = jwt.getClaimAsString("userId");
        long id = Long.parseLong(userId);
        var code = verifyCodeRequestDto.code();
        userService.activateUserAccount(id, code);
        return ResponseEntity.ok("Account activated successfully!");
    }
}

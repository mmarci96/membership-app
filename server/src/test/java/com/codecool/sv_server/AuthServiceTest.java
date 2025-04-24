package com.codecool.sv_server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.service.AuthService;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Test
    void testCreateUser() {
        var email = "test@mail.com";
        var password = "Password123";
        var signupRequest = new SignupRequestDto(email, password);

        var result = authService.registerUser(signupRequest);
        assertEquals(result.email(), email);
        assertNotNull(result.id());
    }

}

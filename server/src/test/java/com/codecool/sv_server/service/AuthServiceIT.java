package com.codecool.sv_server.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.codecool.sv_server.dto.SignupRequestDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceIT {
    @Autowired private AuthService authService;

    @MockBean private EmailService emailService;

    @Test
    void testCreateUserWithValidPassword() {
        var email = "test@mail.com";
        var password = "Password123";
        var name = "test name";
        var signupRequest = new SignupRequestDto(email, name, password);

        var result = authService.registerUser(signupRequest);
        assertEquals(result.email(), email);
        assertNotNull(result.id());
    }

    @Test
    void testEmptyEmailThrowsException() {
        var signupRequest = new SignupRequestDto("", "name", "Password1");
        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.registerUser(signupRequest));
        assertEquals("Email must not be empty", exception.getMessage());
    }

    @Test
    void testInvalidEmailFormatThrowsException() {
        var signupRequest = new SignupRequestDto("notanemail.com", "name", "Password1");
        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.registerUser(signupRequest));
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void testShortPasswordThrowsException() {
        var signupRequest = new SignupRequestDto("test@mail.com", "name", "Ab1");
        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.registerUser(signupRequest));
        assertEquals("Password must be at least 6 characters", exception.getMessage());
    }

    @Test
    void testPasswordWithoutDigitThrowsException() {
        var signupRequest = new SignupRequestDto("test@mail.com", "name", "Password");
        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.registerUser(signupRequest));
        assertEquals("Password must contain at least one digit", exception.getMessage());
    }

    @Test
    void testPasswordWithoutUppercaseThrowsException() {
        var signupRequest = new SignupRequestDto("test@mail.com", "name", "password1");
        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.registerUser(signupRequest));
        assertEquals("Password must contain at least one uppercase letter", exception.getMessage());
    }

    @Test
    void testValidSignupDoesNotThrow() {
        var signupRequest = new SignupRequestDto("valid@mail.com", "name", "Password1");
        assertDoesNotThrow(() -> authService.registerUser(signupRequest));
    }
}

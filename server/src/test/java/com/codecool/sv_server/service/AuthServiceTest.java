package com.codecool.sv_server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.codecool.sv_server.dto.LoginRequestDto;
import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.entity.Role;
import com.codecool.sv_server.entity.User;
import com.codecool.sv_server.repository.UserRepository;
import com.codecool.sv_server.utils.SignupRequestValidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock UserRepository userRepository;

    @Mock PasswordEncoder passwordEncoder;

    @Mock EmailService emailService;

    @InjectMocks AuthService authService;

    @Test
    void givenValidLogin_whenCredentialsMatch_thenReturnsToken() {
        var email = "test@mail.com";
        var password = "Password123";
        var user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword("hashed");
        user.setRole(Role.ROLE_USER);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, "hashed")).thenReturn(true);

        var loginRequest = new LoginRequestDto(email, password);
        var result = authService.validateLogin(loginRequest);

        assertEquals(user.getId(), result.id());
        assertEquals(Role.ROLE_USER, result.role());
    }

    @Test
    void givenValidSignup_whenEmailNotTaken_thenUserSavedAndEmailSent() {
        var signupRequest = new SignupRequestDto("test@mail.com", "Test User", "Password1");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(null);
        when(passwordEncoder.encode("Password1")).thenReturn("hashedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(
                        invocation -> {
                            User user = invocation.getArgument(0);
                            user.setId(1L);
                            return user;
                        });

        try (MockedStatic<SignupRequestValidator> mockValidator =
                mockStatic(SignupRequestValidator.class)) {
            mockValidator
                    .when(() -> SignupRequestValidator.validate(signupRequest))
                    .thenCallRealMethod();
            var response = authService.registerUser(signupRequest);

            assertEquals("test@mail.com", response.email());
            assertNotNull(response.id());
            verify(userRepository).save(any(User.class));
            verify(emailService).sendActivationTokenEmail(anyString(), eq("test@mail.com"));
        }
    }

    @Test
    void givenEmptyEmail_whenSignup_thenThrowsException() {
        var signupRequest = new SignupRequestDto("", "name", "Password1");

        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.registerUser(signupRequest));
        assertEquals("Email must not be empty", exception.getMessage());
    }

    @Test
    void givenInvalidEmail_whenSignup_thenThrowsException() {
        var signupRequest = new SignupRequestDto("notanemail.com", "name", "Password1");

        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.registerUser(signupRequest));
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void givenShortPassword_whenSignup_thenThrowsException() {
        var signupRequest = new SignupRequestDto("test@mail.com", "name", "Ab1");

        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.registerUser(signupRequest));
        assertEquals("Password must be at least 6 characters", exception.getMessage());
    }

    @Test
    void givenPasswordWithoutDigit_whenSignup_thenThrowsException() {
        var signupRequest = new SignupRequestDto("test@mail.com", "name", "Password");

        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.registerUser(signupRequest));
        assertEquals("Password must contain at least one digit", exception.getMessage());
    }

    @Test
    void givenPasswordWithoutUppercase_whenSignup_thenThrowsException() {
        var signupRequest = new SignupRequestDto("test@mail.com", "name", "password1");

        var exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.registerUser(signupRequest));
        assertEquals("Password must contain at least one uppercase letter", exception.getMessage());
    }
}

package com.codecool.sv_server.utils;

import com.codecool.sv_server.dto.SignupRequestDto;

public class SignupRequestValidator {

    public static void validate(SignupRequestDto signupRequestDto) {
        validateEmail(signupRequestDto.email());
        validatePassword(signupRequestDto.password());
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
    }
}

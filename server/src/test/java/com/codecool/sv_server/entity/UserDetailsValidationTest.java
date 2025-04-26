package com.codecool.sv_server.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsValidationTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailValidationForInvalidPhoneNumber() {
        var details = new UserDetails();
        details.setFirstName("John");
        details.setLastName("Doe");
        details.setPhoneNumber("+invalid");
        details.setAddress("123 Street");
        details.setCity("City");
        details.setCountry("hungary");
        details.setId(1L);
        details.setZipCode("abcd");

        Set<ConstraintViolation<UserDetails>> violations = validator.validate(details);

        System.out.println("Violations" + violations);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid phone number")));
    }

    @Test
    void shouldFailValidationForInvalidCountryCode() {
        var details = new UserDetails();
        details.setFirstName("John");
        details.setLastName("Doe");
        details.setPhoneNumber("+36301234567");
        details.setAddress("123 Street");
        details.setCity("City");
        details.setCountry("hungary");
        details.setId(1L);
        details.setZipCode("1234");

        Set<ConstraintViolation<UserDetails>> violations = validator.validate(details);

        System.out.println("Violations: \n" + violations);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid country code")));
    }

    @Test
    void shouldFailValidationForInvalidZipCode() {
        var details = new UserDetails();
        details.setFirstName("John");
        details.setLastName("Doe");
        details.setPhoneNumber("+36301234567");
        details.setAddress("123 Street");
        details.setCity("City");
        details.setCountry("hungary");
        details.setId(1L);
        details.setZipCode("abcd");

        Set<ConstraintViolation<UserDetails>> violations = validator.validate(details);

        System.out.println("Violations" + violations);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid zip code")));
    }
}

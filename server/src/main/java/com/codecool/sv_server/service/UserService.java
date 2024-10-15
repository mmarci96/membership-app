package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.UserSignupDto;
import com.codecool.sv_server.entity.User;
import com.codecool.sv_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean signup(UserSignupDto userSignupDto) {
        // Validate input
        if (userSignupDto.email() == null || userSignupDto.email().isBlank()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        if (userSignupDto.password() == null || userSignupDto.password().length() < 6) {
            return false;
        }

        // Check if the email already exists
        if (userRepository.findByEmail(userSignupDto.email()).isPresent()) {
            return false;
        }

        // Create new user
        User user = new User();
        user.setEmail(userSignupDto.email());
        user.setPassword(passwordEncoder.encode(userSignupDto.password())); // Encode password
        userRepository.save(user);
        return true;
    }
}

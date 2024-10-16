package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.entity.User;
import com.codecool.sv_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public int signup(SignupRequestDto signupRequestDto) {
        // Validate input
        if (signupRequestDto.email() == null || signupRequestDto.email().isBlank()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        if (signupRequestDto.password() == null || signupRequestDto.password().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        // Check if the email already exists
        if (userRepository.findByEmail(signupRequestDto.email()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        // Create new user
        var user = new User();
        user.setEmail(signupRequestDto.email());
        user.setPassword(passwordEncoder.encode(signupRequestDto.password()));
        userRepository.save(user);
        return user.getId();
    }
}

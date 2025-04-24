package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.LoginRequestDto;
import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.dto.SignupResponseDto;
import com.codecool.sv_server.dto.TokenCreateDto;
import com.codecool.sv_server.entity.Role;
import com.codecool.sv_server.entity.User;
import com.codecool.sv_server.repository.UserRepository;
import com.codecool.sv_server.utils.SignupRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public TokenCreateDto validateLogin(LoginRequestDto loginRequestDto) {
        var u = (userRepository.findByEmail(loginRequestDto.email()));
        if (u == null || !passwordEncoder.matches(loginRequestDto.password(), u.getPassword())) {
            return null;
        }
        return new TokenCreateDto(u.getId(), u.getRole());
    }

    @Transactional
    public SignupResponseDto registerUser(SignupRequestDto signupRequestDto) {
        SignupRequestValidator.validate(signupRequestDto);
        if (userRepository.findByEmail(signupRequestDto.email()) != null) {
            return null;
        }
        var user = new User();
        user.setEmail(signupRequestDto.email());
        user.setPassword(passwordEncoder.encode(signupRequestDto.password()));
        user.setEnabled(false);
        user.setActivationToken(UUID.randomUUID().toString());
        user.setActivationExpirationTime(LocalDateTime.now().plusMinutes(90));
        // Default user role when registering
        user.setRole(Role.USER);
        userRepository.save(user);
        emailService.sendActivationTokenEmail(user.getActivationToken(),
                user.getEmail(), user.getId());
        return new SignupResponseDto(user.getEmail(), user.getId());
    }

    @Transactional
    public boolean activateUserAccount(Long userId, String activationToken) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || !user.getActivationToken().equals(activationToken) ||
                user.getActivationExpirationTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        user.setEnabled(true);
        user.setActivationToken(null);
        user.setActivationExpirationTime(null);
        userRepository.save(user);

        return true;
    }

    public void setuserRole(Long id, Role role) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return;
        }
        user.setRole(role);
    }
}

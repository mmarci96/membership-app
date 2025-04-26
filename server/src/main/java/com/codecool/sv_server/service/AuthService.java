package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.LoginRequestDto;
import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.dto.SignupResponseDto;
import com.codecool.sv_server.dto.TokenCreateDto;
import com.codecool.sv_server.entity.Role;
import com.codecool.sv_server.entity.User;
import com.codecool.sv_server.exception.ApiException;
import com.codecool.sv_server.exception.ResourceNotFoundException;
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
    private final PasswordEncoder pwdEncoder;
    private final EmailService emailService;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.pwdEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public TokenCreateDto validateLogin(LoginRequestDto loginRequestDto) {
        var u = (userRepository.findByEmail(loginRequestDto.email()));
        if (u == null) {
            throw new ResourceNotFoundException("user");
        }
        if (!pwdEncoder.matches(loginRequestDto.password(), u.getPassword())) {
            throw new ApiException("Invalid email or password", 401);
        }
        return new TokenCreateDto(u.getId(), u.getRole());
    }

    @Transactional
    public SignupResponseDto registerUser(SignupRequestDto signupRequestDto) {
        SignupRequestValidator.validate(signupRequestDto);
        if (userRepository.findByEmail(signupRequestDto.email()) != null) {
            throw new ApiException("Email already taken!", 409);
        }
        var user = new User();
        user.setEmail(signupRequestDto.email());
        user.setName(signupRequestDto.name());
        user.setPassword(pwdEncoder.encode(signupRequestDto.password()));
        user.setEnabled(false);
        var activationToken = UUID.randomUUID().toString().toUpperCase().substring(0, 6);
        user.setActivationToken(activationToken);
        user.setActivationExpirationTime(LocalDateTime.now().plusMinutes(90));
        user.setRole(Role.USER);
        userRepository.save(user);
        var code = user.getActivationToken();
        var email = user.getEmail();
        emailService.sendActivationTokenEmail(code, email);
        return new SignupResponseDto(email, user.getId());
    }

    @Transactional
    public void activateUserAccount(Long userId, String activationToken) {
        User user = userRepository.findById(userId).orElse(null);
        var now = LocalDateTime.now();
        if (user == null) {
            throw new ResourceNotFoundException("user");
        }
        if (!user.getActivationToken().equals(activationToken)) {
            throw new ApiException("Invalid activation code!", 401);
        }
        if (user.getActivationExpirationTime().isBefore(now)) {
            throw new ApiException("Activation code expired!", 498);
        }
        user.setEnabled(true);
        user.setActivationToken(null);
        user.setActivationExpirationTime(null);
        userRepository.save(user);
    }

    public void setuserRole(Long id, Role role) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return;
        }
        user.setRole(role);
    }
}

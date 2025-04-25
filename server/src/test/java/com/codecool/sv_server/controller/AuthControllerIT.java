package com.codecool.sv_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.codecool.sv_server.dto.LoginRequestDto;
import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.dto.VerifyCodeRequestDto;
import com.codecool.sv_server.repository.UserRepository;
import com.codecool.sv_server.service.EmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private void registerUser(String email, String name, String password) throws Exception {
        var signupRequest = new SignupRequestDto(email, name, password);
        String req = objectMapper.writeValueAsString(signupRequest);
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andExpect(status().isOk());
    }

    private JsonNode loginUser(String email, String password) throws Exception {
        var loginRequest = new LoginRequestDto(email, password);
        String req = objectMapper.writeValueAsString(loginRequest);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.userId").exists())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    @Test
    void test_valid_signup_request() throws Exception {
        registerUser("test@mail.com", "Test Name", "Password1");
    }

    @Test
    void test_invalid_signup_request() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_signup_and_login_recieve_token() throws Exception {
        var email = "test_login@mail.com";
        var password = "Password1";

        registerUser(email, "Test Login", password);
        loginUser(email, password); // expectations handled inside method
    }

    @Test
    void test_activation_with_token_after_register() throws Exception {
        var email = "activate@mail.com";
        var password = "Password1";

        registerUser(email, "Test Activate", password);
        JsonNode loginRes = loginUser(email, password);

        String token = loginRes.get("token").asText();
        long userId = loginRes.get("userId").asLong();

        var user = userRepository.findById(userId);
        var verifyCode = new VerifyCodeRequestDto(user.getActivationToken());
        String codeJson = objectMapper.writeValueAsString(verifyCode);

        mockMvc.perform(post("/api/auth/activate")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(codeJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Account activated successfully!"));
    }

    @Test
    void test_signup_with_existing_email_returns_conflict() throws Exception {
        String email = "duplicate@mail.com";
        registerUser(email, "Test Duplicate", "Password1");

        var signupRequest = new SignupRequestDto(email,
                "Another Nname", "Password1");
        String reqJson = objectMapper.writeValueAsString(signupRequest);
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email already taken!"));
    }

    @Test
    void test_signup_with_invalid_email_format_returns_bad_request() throws Exception {
        var req = new SignupRequestDto("bad-email", "Name", "Password1");
        String reqJson = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid email format"));
    }

    @Test
    void test_signup_with_weak_password_returns_bad_request() throws Exception {
        var req = new SignupRequestDto("weak@mail.com", "Name", "abc");
        String reqJson = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Password must be at least 6 characters"));
    }

    @Test
    void test_login_with_wrong_password_returns_unauthorized() throws Exception {
        String email = "loginfail@mail.com";
        registerUser(email, "Fail Name", "Correct123");

        var login = new LoginRequestDto(email, "Wrong123");
        String reqJson = objectMapper.writeValueAsString(login);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid email or password"));
    }

    @Test
    void test_login_with_nonexistent_user_returns_not_found() throws Exception {
        var login = new LoginRequestDto("doesnotexist@mail.com", "Password1");
        String reqJson = objectMapper.writeValueAsString(login);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found: user"));
    }

    @Test
    void test_activation_with_expired_token_returns_498() throws Exception {
        String email = "expired@mail.com";
        String password = "Password1";

        registerUser(email, "Expired User", password);
        JsonNode loginRes = loginUser(email, password);

        String token = loginRes.get("token").asText();
        long userId = loginRes.get("userId").asLong();

        var user = userRepository.findById(userId);
        user.setActivationExpirationTime(LocalDateTime.now().minusMinutes(1));
        userRepository.save(user);

        var verifyCode = new VerifyCodeRequestDto(user.getActivationToken());
        String codeJson = objectMapper.writeValueAsString(verifyCode);

        mockMvc.perform(post("/api/auth/activate")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(codeJson))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("Activation code expired!"));
    }
}

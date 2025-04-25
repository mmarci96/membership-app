package com.codecool.sv_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        // Optional: you could validate user was saved with userRepository
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
}

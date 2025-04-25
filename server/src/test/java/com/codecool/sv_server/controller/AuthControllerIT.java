package com.codecool.sv_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.aspectj.weaver.ast.And;
import org.checkerframework.checker.units.qual.t;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.codecool.sv_server.dto.LoginRequestDto;
import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.dto.VerifyCodeRequestDto;
import com.codecool.sv_server.repository.UserRepository;
import com.codecool.sv_server.service.EmailService;
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

    @Test
    void test_valid_signup_request() throws Exception {
        var email = "test@mail.com";
        var name = "Test Name";
        var password = "Password1";
        var signupRequestDto = new SignupRequestDto(email, name, password);
        ObjectMapper objectMapper = new ObjectMapper();
        String req = objectMapper.writeValueAsString(signupRequestDto);
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.id").exists());
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
        var name = "Test Login";
        var password = "Password1";
        var signupRequestDto = new SignupRequestDto(email, name, password);
        ObjectMapper objectMapper = new ObjectMapper();
        String req = objectMapper.writeValueAsString(signupRequestDto);
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.id").exists());

        var loginRequestDto = new LoginRequestDto(email, password);
        String loginReq = objectMapper.writeValueAsString(loginRequestDto);
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginReq))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.userId").exists());
    }

    @Test
    void test_activation_with_token_after_register() throws Exception {
        var email = "activate@mail.com";
        var name = "Test Activate";
        var password = "Password1";

        var objectMapper = new ObjectMapper();
        var signupRequest = new SignupRequestDto(email, name, password);
        String signupReqJson = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupReqJson))
                .andExpect(status().isOk());

        var loginRequest = new LoginRequestDto(email, password);
        String loginReqJson = objectMapper.writeValueAsString(loginRequest);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginReqJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String jsonResponse = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(jsonResponse).get("token").asText();
        long id = objectMapper.readTree(jsonResponse).get("userId").asLong();
        var user = userRepository.findById(id);
        var code = new VerifyCodeRequestDto(user.getActivationToken());
        String codeJson = objectMapper.writeValueAsString(code);

        mockMvc.perform(post("/api/auth/activate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(codeJson)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

}

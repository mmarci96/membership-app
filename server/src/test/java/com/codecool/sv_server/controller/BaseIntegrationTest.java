package com.codecool.sv_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codecool.sv_server.dto.LoginRequestDto;
import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.repository.UserRepository;
import com.codecool.sv_server.service.EmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired protected MockMvc mockMvc;

    @Autowired protected UserRepository userRepository;

    @MockBean protected EmailService emailService;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected void registerUser(String email, String name, String password) throws Exception {
        var signupRequest = new SignupRequestDto(email, name, password);
        String req = objectMapper.writeValueAsString(signupRequest);
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(req))
                .andExpect(status().isOk());
    }

    protected JsonNode loginUser(String email, String password) throws Exception {
        var loginRequest = new LoginRequestDto(email, password);
        String req = objectMapper.writeValueAsString(loginRequest);
        MvcResult result =
                mockMvc.perform(
                                post("/api/auth/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(req))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.token").exists())
                        .andExpect(jsonPath("$.userId").exists())
                        .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString());
    }
}

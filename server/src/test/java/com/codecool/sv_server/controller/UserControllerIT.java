package com.codecool.sv_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import com.codecool.sv_server.dto.UserDetailsDto;
import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIT extends BaseIntegrationTest {

    @Test
    public void test_creating_user_details_with_valid_data() throws Exception {
        var email = "user_detail@happy.test";
        var name = "Happy Tester";
        var password = "Password123";
        registerUser(email, name, password);
        JsonNode loginRes = loginUser(email, password);

        String token = loginRes.get("token").asText();
        long userId = loginRes.get("userId").asLong();
        var userDetailsDto = createTestUserDetailsDto(userId);
        var jsonData = objectMapper.writeValueAsString(userDetailsDto);

        mockMvc.perform(post("/api/users/account")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isOk());
    }

    private UserDetailsDto createTestUserDetailsDto(Long userId) {
        return new UserDetailsDto(
                "Test",
                "User",
                "+1234567890",
                "123 Test Street",
                "Testville",
                "TC",
                userId,
                "12345");
    }
}

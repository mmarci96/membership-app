package com.codecool.sv_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codecool.sv_server.dto.UserDetailsDto;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

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

        mockMvc
            .perform(post("/api/users/account")
                         .header("Authorization", "Bearer " + token)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(jsonData))
            .andExpect(status().isOk());
    }

    @Test
    public void test_requesting_userdetailById_without_auth() throws Exception {
        long id = 1L;
        String param = String.valueOf(id);
        mockMvc.perform(get("/api/users/account/" + param))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void test_requesting_userdetailById_with_auth() throws Exception {
        var email = "user_detail_getter@happy.test";
        JsonNode response = postValidUserDetails(email);

        JsonNode loginRes = loginUser(email, "Password1");
        String token = loginRes.get("token").asText();

        long userDetailsId = response.get("userId").asLong();
        mockMvc
            .perform(get("/api/users/account/" + userDetailsId)
                         .header("Authorization", "Bearer " + token)
                         .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(response.toString()))
            .andExpect(status().isOk());
    }

    private JsonNode postValidUserDetails(String email) throws Exception {
        var password = "Password1";
        registerUser(email, "Happy Detailing", password);
        JsonNode loginRes = loginUser(email, password);
        String token = loginRes.get("token").asText();
        long userId = loginRes.get("userId").asLong();
        var userDetailsDto = createTestUserDetailsDto(userId);
        var jsonData = objectMapper.writeValueAsString(userDetailsDto);

        var res = mockMvc
                      .perform(post("/api/users/account")
                                   .header("Authorization", "Bearer " + token)
                                   .contentType(MediaType.APPLICATION_JSON)
                                   .content(jsonData))
                      .andReturn();
        return objectMapper.readTree(res.getResponse().getContentAsString());
    }

    private UserDetailsDto createTestUserDetailsDto(Long userId) {
        return new UserDetailsDto("Test", "User", "+1234567890",
                                  "123 Test Street", "Testville", "TC", userId,
                                  "12345");
    }
}

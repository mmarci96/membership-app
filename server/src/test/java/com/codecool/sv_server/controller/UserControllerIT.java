package com.codecool.sv_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @Test
    public void test_updating_user_details_with_valid_data() throws Exception {
        var email = "update_test@happy.test";
        postValidUserDetails(email);

        JsonNode loginRes = loginUser(email, "Password1");
        String token = loginRes.get("token").asText();
        long userId = loginRes.get("userId").asLong();

        var updatedDetails = new UserDetailsDto(
            "UpdatedFirst", "UpdatedLast", "+9876543210", "456 Updated Street",
            "UpdatedCity", "UC", userId, "67890");
        var updatedJson = objectMapper.writeValueAsString(updatedDetails);

        mockMvc
            .perform(patch("/api/users/account")
                         .header("Authorization", "Bearer " + token)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(updatedJson))
            .andExpect(status().isOk())
            .andExpect(content().json(updatedJson));
    }

    @Test
    public void test_deleting_user_details_with_valid_data() throws Exception {
        var email = "delete_test@happy.test";
        postValidUserDetails(email);

        JsonNode loginRes = loginUser(email, "Password1");
        String token = loginRes.get("token").asText();
        long userId = loginRes.get("userId").asLong();

        var userDetailsDto = new UserDetailsDto(
            "First", "Last", "+1111111111", "111 Delete Street", "DeleteCity",
            "DC", userId, "11111");
        var userDetailsJson = objectMapper.writeValueAsString(userDetailsDto);

        mockMvc
            .perform(delete("/api/users/account/" + userId)
                         .header("Authorization", "Bearer " + token)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(userDetailsJson))
            .andExpect(status().isOk())
            .andExpect(content().string("User details deleted succesfully!"));
    }

    @Test
    public void test_updating_user_details_without_auth_should_fail()
        throws Exception {
        var updatedDetails = new UserDetailsDto("First", "Last", "+1111111111",
                                                "111 Test Street", "City",
                                                "Country", 1L, "11111");
        var updatedJson = objectMapper.writeValueAsString(updatedDetails);

        mockMvc
            .perform(patch("/api/users/account")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(updatedJson))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void test_updating_other_users_details_should_fail()
        throws Exception {
        var email = "unauthorized_update@fail.test";
        postValidUserDetails(email);
        JsonNode loginRes = loginUser(email, "Password1");
        String token = loginRes.get("token").asText();

        var anotherUserId = 9999L;
        var updatedDetails =
            new UserDetailsDto("First", "Last", "+1111111111", "Fake Street",
                               "City", "Country", anotherUserId, "11111");
        var updatedJson = objectMapper.writeValueAsString(updatedDetails);

        mockMvc
            .perform(patch("/api/users/account")
                         .header("Authorization", "Bearer " + token)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(updatedJson))
            .andExpect(status().isForbidden());
    }

    @Test
    public void test_deleting_user_details_without_auth_should_fail()
        throws Exception {
        var userDetailsDto =
            new UserDetailsDto("First", "Last", "+1111111111", "Some Street",
                               "City", "Country", 1L, "12345");
        var userDetailsJson = objectMapper.writeValueAsString(userDetailsDto);

        mockMvc
            .perform(delete("/api/users/account/1")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(userDetailsJson))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void test_deleting_other_users_details_should_fail()
        throws Exception {
        var email = "unauthorized_delete@fail.test";
        postValidUserDetails(email);
        JsonNode loginRes = loginUser(email, "Password1");
        String token = loginRes.get("token").asText();

        var anotherUserId = 9999L;
        var userDetailsDto = new UserDetailsDto(
            "First", "Last", "+1111111111", "Fake Delete Street", "City",
            "Country", anotherUserId, "12345");
        var userDetailsJson = objectMapper.writeValueAsString(userDetailsDto);

        mockMvc
            .perform(delete("/api/users/account/" + anotherUserId)
                         .header("Authorization", "Bearer " + token)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(userDetailsJson))
            .andExpect(status().isForbidden());
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

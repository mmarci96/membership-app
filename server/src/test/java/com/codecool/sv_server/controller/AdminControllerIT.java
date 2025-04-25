package com.codecool.sv_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.codecool.sv_server.dto.CreateBlogPostDto;
import com.codecool.sv_server.dto.LoginRequestDto;
import com.codecool.sv_server.dto.SignupRequestDto;
import com.codecool.sv_server.entity.Role;
import com.codecool.sv_server.entity.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.codecool.sv_server.repository.UserRepository;
import com.codecool.sv_server.service.EmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AdminControllerIT {

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
    void test_valid_blog_post_creation_with_role() throws Exception {
        var name = "Admin Tester";
        var email = "admin@mail.com";
        var password = "Password1";
        registerUser(email, name, password);
        User user = userRepository.findByEmail(email);
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        var title = "Test Title";
        var content = "Test Blogpost Text Content. Lorem ipsum lore.";
        CreateBlogPostDto blogPost = new CreateBlogPostDto(title, content);
        String blogPostJson = objectMapper.writeValueAsString(blogPost);

        var loginRes = loginUser(email, password);
        String token = loginRes.get("token").asText();

        mockMvc.perform(post("/api/admin/blog")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(blogPostJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title));
    }

    @Test
    void test_valid_post_creation_with_invalid_role() throws Exception {
        var name = "User Tester";
        var email = "user@email.com";
        var password = "Password1";
        registerUser(email, name, password);
        var loginRes = loginUser(email, password);
        String token = loginRes.get("token").asText();
        CreateBlogPostDto badBlogPost = new CreateBlogPostDto("Title", "Should fail!");
        String blogPostJson = objectMapper.writeValueAsString(badBlogPost);

        mockMvc.perform(post("/api/admin/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(blogPostJson))
                .andExpect(status().isBadRequest());
    }

}

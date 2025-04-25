package com.codecool.sv_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void test_invalid_title_post_creation_as_content_creator() throws Exception {
        var name = "Content Creator Tester";
        var email = "content_creator@mail.com";
        var password = "Password1";
        registerUser(email, name, password);
        User user = userRepository.findByEmail(email);
        user.setRole(Role.CONTENT_CREATOR);
        userRepository.save(user);

        var title = "";
        var content = "Creating with content creator. Test Blogpost Text Content.";
        CreateBlogPostDto blogPost = new CreateBlogPostDto(title, content);
        String blogPostJson = objectMapper.writeValueAsString(blogPost);

        var loginRes = loginUser(email, password);
        String token = loginRes.get("token").asText();
        mockMvc.perform(post("/api/admin/blog")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(blogPostJson))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("Provide longer title!"));
    }

    @Test
    void test_invalid_content_post_creation_as_content_creator() throws Exception {
        var name = "Content Creator Tester";
        var email = "content_creator_c@mail.com";
        var password = "Password1";
        registerUser(email, name, password);
        User user = userRepository.findByEmail(email);
        user.setRole(Role.CONTENT_CREATOR);
        userRepository.save(user);

        var title = "Valid title.";
        var content = "2short";
        CreateBlogPostDto blogPost = new CreateBlogPostDto(title, content);
        String blogPostJson = objectMapper.writeValueAsString(blogPost);

        var loginRes = loginUser(email, password);
        String token = loginRes.get("token").asText();
        mockMvc.perform(post("/api/admin/blog")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(blogPostJson))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("Provide more content!"));
    }

    @Test
    void test_delete_blogpost_with_valid_user() throws Exception {
        var loginRes = loginUser("admin@mail.com", "Password1");
        String token = loginRes.get("token").asText();

        var title = "Delete me!";
        var content = "This blogpost should be created and deleted";
        CreateBlogPostDto blogPost = new CreateBlogPostDto(title, content);
        String blogPostJson = objectMapper.writeValueAsString(blogPost);

        var result = mockMvc.perform(post("/api/admin/blog")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(blogPostJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode resJson = objectMapper.readTree(result);
        long postId = resJson.get("id").asLong();
        mockMvc.perform(delete("/api/admin/blog/" + postId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/blog/" + postId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}

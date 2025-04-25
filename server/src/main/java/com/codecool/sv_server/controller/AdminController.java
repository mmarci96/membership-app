package com.codecool.sv_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codecool.sv_server.dto.BlogPostDto;
import com.codecool.sv_server.dto.CreateBlogPostDto;
import com.codecool.sv_server.entity.Role;
import com.codecool.sv_server.service.BlogPostService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final BlogPostService blogPostService;

    @Autowired
    public AdminController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping("/blog")
    public ResponseEntity<?> createBlogPost(
            @RequestBody CreateBlogPostDto blogPostDto) {
        System.out.println("Request on /api/admin/blog POST");
        var roleString = "ADMIN";// jwt.getClaimAsString("role");
        Role userRole = null;
        for (Role role : Role.values()) {
            if (roleString == role.name()) {
                userRole = role;
            }
        }
        if (userRole == null) {
            return ResponseEntity.badRequest().body("Role invalid!");
        }
        if (userRole == Role.CONTENT_CREATOR || userRole == Role.ADMIN) {
            var blogPost = blogPostService.createBlogPost(blogPostDto);
            var responseData = new BlogPostDto(blogPost.id(),
                    blogPost.title(), blogPost.content(), blogPost.createdAt());
            return ResponseEntity.ok(responseData);
        }

        return ResponseEntity.badRequest().body("Unexpected error!");
    }

}

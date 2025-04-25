package com.codecool.sv_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codecool.sv_server.dto.BlogPostDto;
import com.codecool.sv_server.dto.BlogPostUpdateDto;
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
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CreateBlogPostDto blogPostDto) {
        var roleString = jwt.getClaimAsString("role");
        Role userRole = Role.valueOf(roleString);
        if (userRole == null) {
            return ResponseEntity.badRequest().body("Role invalid!");
        }
        if (userRole != Role.CONTENT_CREATOR && userRole != Role.ADMIN) {
            return ResponseEntity.badRequest().body("Unauthorized role!");
        }

        var blogPost = blogPostService.createBlogPost(blogPostDto);
        var responseData = new BlogPostDto(blogPost.id(),
                blogPost.title(), blogPost.content(), blogPost.createdAt());
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/blog")
    public ResponseEntity<?> updateBlogPost(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody BlogPostUpdateDto updateData) {
        var roleString = jwt.getClaimAsString("role");
        Role userRole = Role.valueOf(roleString);
        if (userRole == null) {
            return ResponseEntity.badRequest().body("Role invalid!");
        }
        if (userRole != Role.CONTENT_CREATOR && userRole != Role.ADMIN) {
            return ResponseEntity.status(403).body("Unauthorized role!");
        }
        var updatedBlogPost = blogPostService.updateBlogPost(updateData);
        return ResponseEntity.ok(updatedBlogPost);
    }

    @DeleteMapping("/blog/{id}")
    public ResponseEntity<?> deleteBlogPost(@AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        var roleString = jwt.getClaimAsString("role");
        Role userRole = Role.valueOf(roleString);
        if (userRole == null) {
            return ResponseEntity.badRequest().body("Role invalid!");
        }
        if (userRole != Role.CONTENT_CREATOR && userRole != Role.ADMIN) {
            return ResponseEntity.badRequest().body("Unauthorized role!");
        }
        blogPostService.deleteBlogPost(id);
        return ResponseEntity.ok("Blog post deleted succesfully!");
    }

}

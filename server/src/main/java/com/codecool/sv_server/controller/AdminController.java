package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.BlogPostDto;
import com.codecool.sv_server.dto.BlogPostUpdateDto;
import com.codecool.sv_server.dto.CreateBlogPostDto;
import com.codecool.sv_server.service.BlogPostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final BlogPostService blogPostService;

    @Autowired
    public AdminController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping("/blog")
    public ResponseEntity<?> createBlogPost(@RequestBody CreateBlogPostDto blogPostDto) {

        var blogPost = blogPostService.createBlogPost(blogPostDto);
        var responseData =
                new BlogPostDto(
                        blogPost.id(), blogPost.title(), blogPost.content(), blogPost.createdAt());

        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/blog")
    public ResponseEntity<?> updateBlogPost(@RequestBody BlogPostUpdateDto updateData) {

        var updatedBlogPost = blogPostService.updateBlogPost(updateData);
        return ResponseEntity.ok(updatedBlogPost);
    }

    @DeleteMapping("/blog/{id}")
    public ResponseEntity<?> deleteBlogPost(@PathVariable Long id) {
        blogPostService.deleteBlogPost(id);
        return ResponseEntity.ok("Blog post deleted succesfully!");
    }
}

package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.BlogPostDto;
import com.codecool.sv_server.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogPostController {

    private final BlogPostService blogPostService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping("/posts")
    public List<BlogPostDto> getPosts() {
        return blogPostService.getAllBlogPosts().toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDto> getPostById(@PathVariable String id) {
        Long blogPostId = Long.parseLong(id);
        BlogPostDto blogPostDto = blogPostService.getBlogPostById(blogPostId);
        if (blogPostDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(blogPostDto);
    }
}

package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.BlogPostDto;
import com.codecool.sv_server.entity.BlogPost;
import com.codecool.sv_server.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class BlogPostService {
    private final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public Stream<BlogPostDto> getAllBlogPosts() {
        return blogPostRepository.findAll().stream()
                .map(blogPost -> new BlogPostDto(
                        blogPost.getId(),
                        blogPost.getTitle(),
                        blogPost.getContent(),
                        blogPost.getCreatedAt()));
    }

    public BlogPostDto getBlogPostById(Long id) {
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);
        return blogPost.map(post -> new BlogPostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt()))
                .orElse(null);
    }
}

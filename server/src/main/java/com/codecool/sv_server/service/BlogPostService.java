package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.BlogPostDto;
import com.codecool.sv_server.dto.BlogPostUpdateDto;
import com.codecool.sv_server.dto.CreateBlogPostDto;
import com.codecool.sv_server.entity.BlogPost;
import com.codecool.sv_server.exception.ApiException;
import com.codecool.sv_server.exception.ResourceNotFoundException;
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

    public BlogPostDto createBlogPost(CreateBlogPostDto blogPostDto) {
        String title = blogPostDto.title();
        if (title.length() <= 1) {
            throw new ApiException("Provide longer title!", 400);
        }
        String content = blogPostDto.content();
        if (content.length() <= 10) {
            throw new ApiException("Provide more content!", 400);
        }
        var blogPost = new BlogPost();
        blogPost.setTitle(title);
        blogPost.setContent(content);
        blogPostRepository.save(blogPost);
        return new BlogPostDto(blogPost.getId(),
                blogPost.getTitle(),
                blogPost.getContent(),
                blogPost.getCreatedAt());
    }

    public BlogPostDto updateBlogPost(BlogPostUpdateDto updateData) {
        var existing = blogPostRepository.findById(updateData.id());
        if (existing.isPresent()) {
            BlogPost post = existing.get();
            post.setContent(updateData.content());
            post.setTitle(updateData.title());
            blogPostRepository.save(post);
            return new BlogPostDto(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatedAt());
        }
        return null;
    }

    public void deleteBlogPost(Long id) {
        Optional<BlogPost> optionalPost = blogPostRepository.findById(id);
        if (!optionalPost.isPresent()) {
            throw new ResourceNotFoundException("Blog post");
        }

        BlogPost post = optionalPost.get();
        blogPostRepository.delete(post);
    }
}

package com.codecool.sv_server.repository;

import com.codecool.sv_server.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    public BlogPost findByTitle(String title);
}

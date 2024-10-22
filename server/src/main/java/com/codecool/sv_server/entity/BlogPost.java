package com.codecool.sv_server.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "blog_posts")
@Getter
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

}

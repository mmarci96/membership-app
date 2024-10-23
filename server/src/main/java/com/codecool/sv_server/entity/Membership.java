package com.codecool.sv_server.entity;

import jakarta.persistence.*;

@Entity
public class Membership {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}

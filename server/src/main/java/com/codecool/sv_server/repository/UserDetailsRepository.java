package com.codecool.sv_server.repository;

import com.codecool.sv_server.entity.UserDetails;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository
    extends JpaRepository<UserDetails, Long> {
    UserDetails findByUserId(Long user_id);
}

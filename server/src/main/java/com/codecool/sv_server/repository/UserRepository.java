package com.codecool.sv_server.repository;

import com.codecool.sv_server.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

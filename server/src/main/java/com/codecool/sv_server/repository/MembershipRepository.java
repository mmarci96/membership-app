package com.codecool.sv_server.repository;

import com.codecool.sv_server.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

}

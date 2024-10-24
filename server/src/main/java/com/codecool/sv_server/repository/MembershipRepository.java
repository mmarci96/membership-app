package com.codecool.sv_server.repository;

import com.codecool.sv_server.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    @Query("SELECT m FROM Membership m WHERE m.user.email = :email AND m.subscriptionStatus = 'ACTIVE'")
    Membership findActiveByUserEmail(String email);
}

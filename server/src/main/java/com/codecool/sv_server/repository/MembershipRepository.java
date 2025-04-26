package com.codecool.sv_server.repository;

import com.codecool.sv_server.entity.Membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    @Query(
            "SELECT m FROM Membership m JOIN m.user u WHERE u.email = :email AND"
                + " m.subscriptionStatus = com.codecool.sv_server.entity.SubscriptionStatus.ACTIVE")
    Membership findActiveByUserEmail(@Param("email") String email);
}

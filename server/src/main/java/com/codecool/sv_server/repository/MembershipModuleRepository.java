package com.codecool.sv_server.repository;

import com.codecool.sv_server.entity.MembershipModule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipModuleRepository extends JpaRepository<MembershipModule, Long> {
    List<MembershipModule> findByMembershipPackageId(Long packageId);
}

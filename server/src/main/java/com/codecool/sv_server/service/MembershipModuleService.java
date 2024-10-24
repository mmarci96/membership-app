package com.codecool.sv_server.service;
import com.codecool.sv_server.entity.MembershipModule;
import com.codecool.sv_server.repository.MembershipModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembershipModuleService {

    private final MembershipModuleRepository membershipModuleRepository;

    public MembershipModuleService(MembershipModuleRepository membershipModuleRepository) {
        this.membershipModuleRepository = membershipModuleRepository;
    }

    // Create a new module
    public MembershipModule createModule(MembershipModule membershipModule) {
        return membershipModuleRepository.save(membershipModule);
    }

    // Get all modules by package
    public List<MembershipModule> getModulesByPackageId(Long packageId) {
        // Implement a method in the repository to fetch modules by package ID
        return membershipModuleRepository.findByMembershipPackageId(packageId);
    }

    // Find a module by ID
    public Optional<MembershipModule> getModuleById(Long id) {
        return membershipModuleRepository.findById(id);
    }

    // Update a module
    public MembershipModule updateModule(MembershipModule membershipModule) {
        return membershipModuleRepository.save(membershipModule);
    }

    // Delete a module
    public void deleteModule(Long id) {
        membershipModuleRepository.deleteById(id);
    }
}
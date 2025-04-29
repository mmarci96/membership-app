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

    public MembershipModule createModule(MembershipModule membershipModule) {
        return membershipModuleRepository.save(membershipModule);
    }

    public List<MembershipModule> getModulesByPackageId(Long packageId) {
        return membershipModuleRepository.findByMembershipPackageId(packageId);
    }

    public Optional<MembershipModule> getModuleById(Long id) {
        return membershipModuleRepository.findById(id);
    }

    public MembershipModule updateModule(MembershipModule membershipModule) {
        return membershipModuleRepository.save(membershipModule);
    }

    public void deleteModule(Long id) {
        membershipModuleRepository.deleteById(id);
    }
}

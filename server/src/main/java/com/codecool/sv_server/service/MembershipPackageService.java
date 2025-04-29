package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.MembershipModuleDTO;
import com.codecool.sv_server.dto.MembershipPackageDTO;
import com.codecool.sv_server.entity.MembershipPackage;
import com.codecool.sv_server.repository.MembershipPackageRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipPackageService {

    private final MembershipPackageRepository membershipPackageRepository;

    public MembershipPackageService(MembershipPackageRepository membershipPackageRepository) {
        this.membershipPackageRepository = membershipPackageRepository;
    }

    public MembershipPackageDTO getPackageById(Long packageId) {
        var membershipPackage = membershipPackageRepository.findById(packageId).orElse(null);
        if (membershipPackage == null) {
            return null;
        }

        return new MembershipPackageDTO(
                membershipPackage.getId(),
                membershipPackage.getName(),
                membershipPackage.getModules().stream()
                        .map(
                                module ->
                                        new MembershipModuleDTO(
                                                module.getId(),
                                                module.getTitle(),
                                                module.getContent()))
                        .toList());
    }

    public MembershipPackage createPackage(MembershipPackage membershipPackage) {
        return membershipPackageRepository.save(membershipPackage);
    }

    public List<MembershipPackage> getAllPackages() {
        return membershipPackageRepository.findAll();
    }

    public MembershipPackage updatePackage(MembershipPackage membershipPackage) {
        return membershipPackageRepository.save(membershipPackage);
    }

    public void deletePackage(Long id) {
        membershipPackageRepository.deleteById(id);
    }
}

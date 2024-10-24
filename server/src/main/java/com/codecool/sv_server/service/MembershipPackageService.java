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
        // Logic to fetch the MembershipPackage entity and convert to DTO
        var membershipPackage =
                membershipPackageRepository.findById(packageId).orElse(null);
        if (membershipPackage == null) {
            return null;
        }
        // Convert the entity to DTO (you can use ModelMapper or manually map the fields)

        // Populate the list of modules (convert them to DTOs as well)
        // Add this logic to map the modules if needed
        // packageDTO.setModules(...);

        return new MembershipPackageDTO(
                membershipPackage.getId(),
                membershipPackage.getName(),
                membershipPackage.getModules()
                                 .stream()
                                 .map(module ->
                                     new MembershipModuleDTO(module.getId(),
                                                             module.getTitle(),
                                                             module.getContent()))
                                 .toList());
    }

    // Create a new membership package
    public MembershipPackage createPackage(MembershipPackage membershipPackage) {
        return membershipPackageRepository.save(membershipPackage);
    }

    // Get all membership packages
    public List<MembershipPackage> getAllPackages() {
        return membershipPackageRepository.findAll();
    }


    // Update a membership package
    public MembershipPackage updatePackage(MembershipPackage membershipPackage) {
        return membershipPackageRepository.save(membershipPackage);
    }

    // Delete a membership package
    public void deletePackage(Long id) {
        membershipPackageRepository.deleteById(id);
    }
}

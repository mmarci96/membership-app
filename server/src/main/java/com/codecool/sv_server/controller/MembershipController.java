package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.MembershipPackageDTO;
import com.codecool.sv_server.dto.MembershipPackageNameDto;
import com.codecool.sv_server.dto.MembershipStatusDto;
import com.codecool.sv_server.service.MembershipPackageService;
import com.codecool.sv_server.service.MembershipService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {
    private final MembershipService membershipService;
    private final MembershipPackageService membershipPackageService;

    @Autowired
    public MembershipController(
            MembershipService membershipService,
            MembershipPackageService membershipPackageService) {
        this.membershipService = membershipService;
        this.membershipPackageService = membershipPackageService;
    }

    @GetMapping("/packages/{id}")
    public ResponseEntity<MembershipPackageDTO> getMembershipPackageContent(@PathVariable Long id) {

        MembershipPackageDTO membershipPackageDTO = membershipPackageService.getPackageById(id);
        return ResponseEntity.ok(membershipPackageDTO);
    }

    @GetMapping("/packages")
    public ResponseEntity<List<MembershipPackageNameDto>> getMembershipPackages() {
        List<MembershipPackageNameDto> packageNameDtoList =
                membershipPackageService.getAllPackages().stream()
                        .map(
                                membershipPackage ->
                                        new MembershipPackageNameDto(
                                                membershipPackage.getId(),
                                                membershipPackage.getName()))
                        .toList();
        return ResponseEntity.ok(packageNameDtoList);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<MembershipStatusDto> getMembershipStatus(@PathVariable long user_id) {
        var status = membershipService.getMemberShipStatus(user_id);
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(status);
    }
}

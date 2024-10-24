package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.MembershipPackageDTO;
import com.codecool.sv_server.dto.MembershipPackageNameDto;
import com.codecool.sv_server.dto.MembershipStatusDto;
import com.codecool.sv_server.dto.SubscriptionReqDto;
import com.codecool.sv_server.entity.Membership;
import com.codecool.sv_server.service.MembershipPackageService;
import com.codecool.sv_server.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {
    private final MembershipService membershipService;
    private final MembershipPackageService membershipPackageService;
    @Autowired
    public MembershipController(MembershipService membershipService,
                                MembershipPackageService membershipPackageService) {
        this.membershipService = membershipService;
        this.membershipPackageService = membershipPackageService;
    }
    @GetMapping("/packages/{membershipPackageId}")
    public ResponseEntity<MembershipPackageDTO> getMembershipPackageContent(
            @PathVariable Long membershipPackageId,
            @AuthenticationPrincipal String userEmail) {

        // Check if the user has an active membership
        Membership activeMembership = membershipService.findActiveMembershipByEmail(userEmail);

        if (activeMembership == null) {
            return ResponseEntity.status(403).body(null);  // Forbidden, user does not have active membership
        }

        // Fetch the requested membership package
        MembershipPackageDTO membershipPackageDTO = membershipPackageService.getPackageById(membershipPackageId);
        return ResponseEntity.ok(membershipPackageDTO);
    }

    @GetMapping("/packages")
    public ResponseEntity<List<MembershipPackageNameDto>> getMembershipPackages() {
        List<MembershipPackageNameDto> packageNameDtoList =
                membershipPackageService.getAllPackages()
                        .stream().map(membershipPackage ->
                                new MembershipPackageNameDto(
                                        membershipPackage.getId(),
                                        membershipPackage.getName()))
                        .toList();
        return ResponseEntity.ok(packageNameDtoList);
    }

    @GetMapping
    public ResponseEntity<MembershipStatusDto> getMembershipStatus(
            @RequestBody SubscriptionReqDto subscriptionReqDto) {
        long id = subscriptionReqDto.userId();
        var status = membershipService.getMemberShipStatus(id);
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(status);
    }
    @PostMapping
    public ResponseEntity<MembershipStatusDto> createMembershipStatus(
            @RequestBody SubscriptionReqDto subscriptionReqDto) {
        if(subscriptionReqDto.paymentStatus()){
            var status =
                    membershipService.startMembership(subscriptionReqDto.userId());
            return ResponseEntity.ok(status);
        }
        return ResponseEntity.status(404).build();
    }
}

package com.codecool.sv_server.controller;

import com.codecool.sv_server.dto.MembershipStatusDto;
import com.codecool.sv_server.dto.SubscriptionReqDto;
import com.codecool.sv_server.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/membership")
public class MembershipController {
    private final MembershipService membershipService;

    @Autowired
    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
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

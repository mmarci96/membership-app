package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.MembershipStatusDto;
import com.codecool.sv_server.dto.SubscriptionReqDto;
import com.codecool.sv_server.entity.Membership;
import com.codecool.sv_server.entity.SubscriptionStatus;
import com.codecool.sv_server.entity.User;
import com.codecool.sv_server.exception.ApiException;
import com.codecool.sv_server.exception.ResourceNotFoundException;
import com.codecool.sv_server.repository.MembershipRepository;
import com.codecool.sv_server.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

    @Autowired
    public MembershipService(
            MembershipRepository membershipRepository, UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
    }

    public MembershipStatusDto startMembership(SubscriptionReqDto subRequestDto) {
        if (!subRequestDto.paymentStatus()) {
            throw new ApiException("Payment unsuccessful", 400);
        }
        var userId = subRequestDto.userId();
        var existing = membershipRepository.findByUserId(userId);
        if (existing != null) {
            throw new ApiException("Already started membership", 400);
        }
        User user = userRepository.findById(userId);
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        membership.setEndDate(LocalDate.now().plusMonths(1));

        membershipRepository.save(membership);
        var subStatus = membership.getSubscriptionStatus();
        var endDate = membership.getEndDate();
        return new MembershipStatusDto(userId, subStatus, endDate);
    }

    public MembershipStatusDto getMemberShipStatus(long userId) {
        User user = userRepository.findById(userId);
        Membership membership = user.getMembership();
        if (membership == null) {
            throw new ResourceNotFoundException("membership");
        }
        var subStatus = membership.getSubscriptionStatus();
        var endDate = membership.getEndDate();
        return new MembershipStatusDto(userId, subStatus, endDate);
    }

    public Membership findActiveMembershipByEmail(String email) {
        return membershipRepository.findActiveByUserEmail(email);
    }
}

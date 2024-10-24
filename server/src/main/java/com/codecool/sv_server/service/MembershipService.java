package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.MembershipStatusDto;
import com.codecool.sv_server.entity.Membership;
import com.codecool.sv_server.entity.SubscriptionStatus;
import com.codecool.sv_server.entity.User;
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
    public MembershipService(MembershipRepository membershipRepository, UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
    }

    public MembershipStatusDto startMembership(long userId) {
        User u = userRepository.findById(userId);
        Membership membership = new Membership();
        membership.setUser(u);
        membership.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        membership.setEndDate(LocalDate.now().plusMonths(1));

        membershipRepository.save(membership);
        return new MembershipStatusDto(
                userId,
                membership.getSubscriptionStatus(),
                membership.getEndDate()
        );

    }

    public MembershipStatusDto getMemberShipStatus(long userId){
        User user = userRepository.findById(userId);
        Membership membership = user.getMembership();
        if (membership == null) {
            return null;
        }
        return new MembershipStatusDto(
                userId,
                membership.getSubscriptionStatus(),
                membership.getEndDate()
        );
    }
    public Membership findActiveMembershipByEmail(String email) {
        return membershipRepository.findActiveByUserEmail(email);
    }

}

package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.MembershipStatusDto;
import com.codecool.sv_server.entity.Membership;
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

    public MembershipStatusDto startMembership(String email) {
        User user = userRepository.findByEmail(email);
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setActive(true);
        membership.setEndDate(LocalDate.now().plusMonths(1));
        membershipRepository.save(membership);
        return new MembershipStatusDto(email, membership.isActive(), membership.getEndDate());
    }

    public MembershipStatusDto getMembershipStatus(String email) {
        User user = userRepository.findByEmail(email);
        Membership membership = user.getMembership();
        if (membership == null) {
            return null;
        }
        return new MembershipStatusDto(email, membership.isActive(), membership.getEndDate());
    }

}

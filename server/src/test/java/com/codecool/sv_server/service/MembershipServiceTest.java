package com.codecool.sv_server.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.codecool.sv_server.dto.MembershipStatusDto;
import com.codecool.sv_server.dto.SubscriptionReqDto;
import com.codecool.sv_server.entity.Membership;
import com.codecool.sv_server.entity.SubscriptionStatus;
import com.codecool.sv_server.entity.User;
import com.codecool.sv_server.exception.ApiException;
import com.codecool.sv_server.exception.ResourceNotFoundException;
import com.codecool.sv_server.repository.MembershipRepository;
import com.codecool.sv_server.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

    @Mock UserRepository userRepository;

    @Mock MembershipRepository membershipRepository;

    @InjectMocks MembershipService membershipService;

    @Test
    public void startMembership_shouldCreateNewMembership() {
        Long userId = 1L;
        SubscriptionReqDto reqDto = new SubscriptionReqDto(userId, true, "Payment-intent");
        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        MembershipStatusDto result = membershipService.startMembership(reqDto);

        assertEquals(userId, result.userId());
        assertEquals(SubscriptionStatus.ACTIVE, result.status());
        assertNotNull(result.endDate());
        verify(membershipRepository).save(any(Membership.class));
    }

    @Test
    public void startMembership_shouldThrowIfAlreadyHasMembership() {
        Long userId = 1L;
        SubscriptionReqDto reqDto = new SubscriptionReqDto(userId, true, "package");

        when(membershipRepository.findByUserId(userId)).thenReturn(new Membership());

        ApiException ex =
                assertThrows(
                        ApiException.class,
                        () -> {
                            membershipService.startMembership(reqDto);
                        });

        assertEquals("Already started membership", ex.getMessage());
    }

    @Test
    public void startMembership_shouldThrowIfPaymentFails() {
        Long userId = 1L;
        SubscriptionReqDto reqDto = new SubscriptionReqDto(userId, false, "Payment");

        ApiException ex =
                assertThrows(
                        ApiException.class,
                        () -> {
                            membershipService.startMembership(reqDto);
                        });

        assertEquals("Payment unsuccessful", ex.getMessage());
    }

    @Test
    public void getMemberShipStatus_shouldReturnActiveMembership() {
        Long userId = 1L;
        User mockUser = new User();
        Membership membership = new Membership();
        membership.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        membership.setEndDate(LocalDate.now().plusDays(30));
        mockUser.setMembership(membership);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        MembershipStatusDto result = membershipService.getMemberShipStatus(userId);

        assertEquals(userId, result.userId());
        assertEquals(SubscriptionStatus.ACTIVE, result.status());
        assertNotNull(result.endDate());
    }

    @Test
    public void getMemberShipStatus_shouldThrowIfNoMembership() {
        Long userId = 1L;
        User mockUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        ResourceNotFoundException ex =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> {
                            membershipService.getMemberShipStatus(userId);
                        });

        assertEquals("Resource not found: membership", ex.getMessage());
    }
}

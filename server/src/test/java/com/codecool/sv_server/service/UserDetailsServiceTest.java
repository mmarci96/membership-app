package com.codecool.sv_server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.codecool.sv_server.dto.UserDetailsDto;
import com.codecool.sv_server.entity.User;
import com.codecool.sv_server.entity.UserDetails;
import com.codecool.sv_server.exception.ApiException;
import com.codecool.sv_server.repository.UserDetailsRepository;
import com.codecool.sv_server.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {
    @Mock UserRepository userRepository;

    @Mock UserDetailsRepository userDetailsRepository;

    @InjectMocks UserDetailsService userDetailsService;

    @Test
    void test_create_userDetail_with_valid_data() {
        var id = 1L;
        User user = new User();
        user.setId(id);
        user.setEmail("valid@email.com");

        UserDetailsDto detailsDto = createTestUserDetailsDto(user.getId());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userDetailsRepository.save(any(UserDetails.class)))
                .thenAnswer(
                        inv -> {
                            UserDetails d = inv.getArgument(0);
                            d.setId(1L);
                            return d;
                        });

        var result = userDetailsService.createUserDetails(detailsDto);
        System.out.println("Result: " + result);
        assertNotNull(result);
    }

    @Test
    void test_create_userDetail_with_no_user() {
        var badId = 99L;
        UserDetailsDto userDetailsDto = createTestUserDetailsDto(badId);
        when(userRepository.findById(badId)).thenReturn(Optional.empty());
        var ex =
                assertThrows(
                        ApiException.class,
                        () -> {
                            userDetailsService.createUserDetails(userDetailsDto);
                        });
        String expectedMsg = "User does not exist!";
        String actualMsg = ex.getMessage();
        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    void test_update_userDetails_with_valid_user_and_data() {
        var id = 1L;
        User user = new User();
        user.setId(id);
        user.setEmail("valid_test@email.com");
        UserDetailsDto detailsDto = createTestUserDetailsDto(user.getId());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userDetailsRepository.save(any(UserDetails.class)))
                .thenAnswer(
                        inv -> {
                            UserDetails d = inv.getArgument(0);
                            d.setId(id);
                            return d;
                        });
        var details = userDetailsService.createUserDetails(detailsDto);

        UserDetailsDto updateData =
                new UserDetailsDto(
                        "mmmm", "mmmm", "+363043988", "mmmmmm", "Budapest", "HU", id, "2112");

        UserDetails userDetails = new UserDetails();
        userDetails.setId(id);
        userDetails.setUser(user);
        userDetails.setLastName("mmmm");
        userDetails.setFirstName("mmmm");

        when(userDetailsRepository.findById(id)).thenReturn(Optional.of(userDetails));

        var result = userDetailsService.updateUserDetails(updateData, user.getId());

        assertNotNull(result);
        assertEquals("mmmm", result.firstName());
        assertEquals("mmmm", result.lastName());
        assertEquals("Budapest", result.city());
        assertEquals("HU", result.country());
        assertEquals("2112", result.zipCode());
        assertTrue(result != details);
    }

    private UserDetailsDto createTestUserDetailsDto(Long userId) {
        return new UserDetailsDto(
                "Test",
                "User",
                "+1234567890",
                "123 Test Street",
                "Testville",
                "TC",
                userId,
                "12345");
    }
}

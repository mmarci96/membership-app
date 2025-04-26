package com.codecool.sv_server.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codecool.sv_server.dto.UserDetailsDto;
import com.codecool.sv_server.entity.User;
import com.codecool.sv_server.entity.UserDetails;
import com.codecool.sv_server.repository.UserDetailsRepository;
import com.codecool.sv_server.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserDetailsRepository userDetailsRepository;

    @InjectMocks
    UserDetailsService userDetailsService;

    void test_create_userDetail_with_valid_data() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setEmail("valid@email.com");

        UserDetailsDto detailsDto = createTestUserDetailsDto(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userDetailsRepository.save(any(UserDetails.class))).thenAnswer(inv -> {
            UserDetails d = inv.getArgument(0);
            d.setId(1L);
            return d;
        });

        var result = userDetailsService.createUserDetails(detailsDto);
        assertNotNull(result);
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

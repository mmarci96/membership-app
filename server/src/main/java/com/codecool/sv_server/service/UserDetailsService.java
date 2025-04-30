package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.UserDetailsDto;
import com.codecool.sv_server.entity.UserDetails;
import com.codecool.sv_server.exception.ApiException;
import com.codecool.sv_server.exception.ResourceNotFoundException;
import com.codecool.sv_server.repository.UserDetailsRepository;
import com.codecool.sv_server.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService {
    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsService(
            UserDetailsRepository userDetailsRepository, UserRepository userRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.userRepository = userRepository;
    }

    public UserDetailsDto getUserDetailsByUserId(long userId) {
        var u = userDetailsRepository.findByUserId(userId);
        if (u == null) {
            throw new ResourceNotFoundException("user detail");
        }
        return new UserDetailsDto(
                u.getFirstName(),
                u.getLastName(),
                u.getPhoneNumber(),
                u.getAddress(),
                u.getCity(),
                u.getCountry(),
                userId,
                u.getZipCode());
    }

    public UserDetailsDto createUserDetails(UserDetailsDto userDetailsDto) {
        var user = userRepository.findById(userDetailsDto.userId()).orElse(null);
        if (user == null) {
            throw new ApiException("User does not exist!", 404);
        }
        var userDetails = new UserDetails();
        userDetails.setUser(user);
        userDetails.setFirstName(userDetailsDto.firstName());
        userDetails.setLastName(userDetailsDto.lastName());
        userDetails.setPhoneNumber(userDetailsDto.phoneNumber());
        userDetails.setAddress(userDetailsDto.address());
        userDetails.setCity(userDetailsDto.city());
        userDetails.setCountry(userDetailsDto.country());
        userDetails.setZipCode(userDetailsDto.zipCode());

        userDetailsRepository.save(userDetails);
        return userDetailsDto;
    }

    public UserDetailsDto updateUserDetails(UserDetailsDto updateData, Long id) {
        Optional<UserDetails> existing = userDetailsRepository.findById(id);
        System.out.println("EXISTING: " + existing);
        if (existing.isEmpty()) {
            throw new ResourceNotFoundException("user detail");
        }
        UserDetails details = existing.get();
        details.setCity(updateData.city());
        details.setFirstName(updateData.firstName());
        details.setLastName(updateData.lastName());
        details.setPhoneNumber(updateData.phoneNumber());
        details.setAddress(updateData.address());
        details.setCountry(updateData.country());
        details.setZipCode(updateData.zipCode());

        userDetailsRepository.save(details);
        return new UserDetailsDto(
                details.getFirstName(),
                details.getLastName(),
                details.getPhoneNumber(),
                details.getAddress(),
                details.getCity(),
                details.getCountry(),
                details.getId(),
                details.getZipCode());
    }

    public void deleteUser(Long id) {
        userDetailsRepository.deleteById(id);
    }
}

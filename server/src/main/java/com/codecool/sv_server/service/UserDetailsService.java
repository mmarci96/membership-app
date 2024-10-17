package com.codecool.sv_server.service;

import com.codecool.sv_server.dto.UserDetailsDto;
import com.codecool.sv_server.entity.UserDetails;
import com.codecool.sv_server.repository.UserDetailsRepository;
import com.codecool.sv_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {
    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    @Autowired
    public UserDetailsService(UserDetailsRepository userDetailsRepository, UserRepository userRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.userRepository = userRepository;
    }

    public UserDetailsDto getUserDetails(long userId) {
        var u = userDetailsRepository.findById(userId).orElse(null);
        if (u == null) {
            return null;
        }
        return new UserDetailsDto(
                u.getFirstName(),u.getLastName(), u.getPhoneNumber(),
                u.getAddress(), u.getCity(), u.getCountry(), userId
        );
    }
    public UserDetailsDto setupUserDetails(UserDetailsDto userDetailsDto) {
        var u = userRepository.findById(userDetailsDto.id());
        if (u == null) {
            return null;
        }
        var userDetails = new UserDetails();
        userDetails.setUser(u);
        userDetails.setFirstName(userDetailsDto.firstName());
        userDetails.setLastName(userDetailsDto.lastName());
        userDetails.setPhoneNumber(userDetailsDto.phoneNumber());
        userDetails.setAddress(userDetailsDto.address());
        userDetails.setCity(userDetailsDto.city());
        userDetails.setCountry(userDetailsDto.country());

        userDetailsRepository.save(userDetails);
        return userDetailsDto;
    }
}

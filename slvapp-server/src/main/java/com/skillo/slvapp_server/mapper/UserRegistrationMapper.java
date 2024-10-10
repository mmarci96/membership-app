package com.skillo.slvapp_server.mapper;

import com.skillo.slvapp_server.dto.RegistrationRequestDto;
import com.skillo.slvapp_server.dto.RegistrationResponseDto;
import com.skillo.slvapp_server.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper {

//  public User toEntity(RegistrationRequestDto registrationRequestDto) {
//      final var user = new User();
//      user.setEmail(registrationRequestDto.email());
//      user.setUsername(registrationRequestDto.username());
//      user.setPassword(registrationRequestDto.password());
//      return user;
//  }

    public RegistrationResponseDto toRegistrationResponseDto(final User user) {
        return new RegistrationResponseDto(
                user.getEmail(), user.getUsername());
    }
}

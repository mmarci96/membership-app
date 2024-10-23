package com.codecool.sv_server.dto;

public record UserDetailsDto(String firstName, String lastName,
                             String phoneNumber, String address, String city,
                             String country, long userId) {
}

package com.codecool.sv_server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_details")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDetails {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 80)
    @Column(nullable = false)
    private String firstName;

    @Size(max = 80)
    @Column(nullable = false)
    private String lastName;

    @Size(max = 15)
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number")
    @Column(nullable = false)
    private String phoneNumber;

    @Size(max = 120)
    @Column(nullable = false)
    private String address;

    @Size(max = 80)
    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Z]{2,3}$", message = "Invalid country code")
    private String country;

    @Pattern(regexp = "^[0-9]{4,6}$", message = "Invalid zip code")
    @Column(nullable = false)
    private String zipCode;
}

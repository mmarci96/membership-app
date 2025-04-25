package com.codecool.sv_server.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.codecool.sv_server.dto.TokenInfo;
import com.codecool.sv_server.entity.Role;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    private final JwtEncoder encoder;
    private final JwtDecoder jwtDecoder;

    public TokenService(JwtEncoder encoder, JwtDecoder jwtDecoder) {
        this.encoder = encoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(String email, Role role, Long userId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:5173")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(email)
                .claim("role", role.name())
                .claim("userId", userId)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public TokenInfo extractTokenInfo(String token) {
        Jwt decoded = jwtDecoder.decode(token);

        String email = decoded.getSubject();
        Long userId = decoded.getClaim("userId");
        Role role = Role.valueOf(decoded.getClaim("role"));

        return new TokenInfo(email, userId, role);
    }
}

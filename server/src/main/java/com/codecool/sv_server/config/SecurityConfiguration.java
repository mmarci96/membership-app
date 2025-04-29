package com.codecool.sv_server.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final RsaKeyProperties rsaKeys;

    public SecurityConfiguration(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request
                                   -> request.requestMatchers("/api/auth/*")
                                          .permitAll()
                                          .requestMatchers("/api/blog/*")
                                          .permitAll()
                                          .requestMatchers("/api/stripe/*")
                                          .permitAll()
                                          .requestMatchers("/hello")
                                          .permitAll()
                                          .requestMatchers("/health")
                                          .permitAll()
                                          .requestMatchers("/ping")
                                          .permitAll()
                                          .anyRequest()
                                          .authenticated())
            .oauth2ResourceServer(
                oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .sessionManagement(session
                               -> session.sessionCreationPolicy(
                                   SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())
            .build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey())
                      .privateKey(rsaKeys.privateKey())
                      .build();
        JWKSource<SecurityContext> jwkSource =
            new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.DELETE);
        configuration.addAllowedMethod(HttpMethod.OPTIONS);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner logEndpoints(@Qualifier(
        "requestMappingHandlerMapping") RequestMappingHandlerMapping mapping) {
        return args -> {
            System.out.println(
                "+-----------+-------------------------------------+");
            System.out.printf("| %-10s| %-36s|%n", "Method", "Endpoint");
            System.out.println(
                "+-----------+-------------------------------------+");

            mapping.getHandlerMethods().forEach((requestMappingInfo,
                                                 handlerMethod) -> {
                var methods =
                    requestMappingInfo.getMethodsCondition().getMethods();
                var patterns =
                    requestMappingInfo.getPathPatternsCondition().getPatterns();

                String methodStr = methods.isEmpty()
                    ? "ANY"
                    : methods.stream()
                          .map(Enum::name)
                          .reduce((a, b) -> a + "," + b)
                          .orElse("ANY");

                for (var pattern : patterns) {
                    System.out.printf("| %-10s| %-36s|%n", methodStr, pattern);
                    System.out.println(
                        "+-----------+-------------------------------------+");
                }
            });
        };
    }
}

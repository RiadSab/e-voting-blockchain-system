package com.evote.backend.config.springConfig;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasRole("admin")
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakAuthConverter()))
                );

        return http.build();
    }

    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> keycloakAuthConverter() {
        return jwt -> {
            Set<String> roles = extractClientRoles(jwt, "evote-backend");
            var authorities = roles.stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toSet());
            return new JwtAuthenticationToken(jwt, authorities);
        };
    }

    private Set<String> extractClientRoles(Jwt jwt, String clientId) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) return Set.of();

        Object clientAccessObj = resourceAccess.get(clientId);
        if (!(clientAccessObj instanceof Map<?, ?> clientAccess)) return Set.of();

        Object rolesObj = clientAccess.get("roles");
        if (!(rolesObj instanceof Collection<?> roles)) return Set.of();

        Set<String> out = new HashSet<>();
        for (Object r : roles) out.add(String.valueOf(r));
        return out;
    }
}
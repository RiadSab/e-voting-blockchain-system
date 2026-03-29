package com.evote.backend.security.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class KeycloakRoleConverter  implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.isEmpty() || !realmAccess.containsKey("roles")) {
            return List.of();
        }

        return ((List<String>) realmAccess.get("roles"))
                .stream()
                .map(roleName -> (GrantedAuthority) () -> "ROLE_" + roleName)
                .toList();

    }
}

package com.example.tickets.config;

import jakarta.persistence.Convert;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
/*
This class converts a JWT token into a Spring Security authentication object
with roles/authorities so that Spring can perform authorization

What the JwtAuthenticationConverter does:
1. Receives a JWT from the current user making a request.
2. Extracts the roles from the realm_access.roles claim inside the JWT.
3. Filters roles so only ones starting with ROLE_ remain.
4. Converts those roles into SimpleGrantedAuthority objects (Spring Security authorities).
5. Creates a JwtAuthenticationToken containing:
    - the JWT
    - the user's authorities.
6. Spring stores this in the SecurityContext so it can check permissions (e.g. @PreAuthorize("hasRole('ORGANIZER')")).
 */
public class JwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {    // Converts a JWT token to Spring's JwtAuthenticationToken
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    // Pulls out the role from the JWT claims
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access"); // The realm_access claim contains the roles
        // The map looks something like this
        // {
        //   "roles": [
        //      "default-roles-event-ticket-platform",
        //      "offline_access",
        //      "uma_authorization",
        //      "ROLE_ORGANIZER"
        //   ]
        //}


        if(null == realmAccess || !realmAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>)realmAccess.get("roles");
        // Now we extract the value of the "roles" key, so roles becomes:
        // [
        // "default-roles-event-ticket-platform",
        // "offline_access",
        // "uma_authorization",
        // "ROLE_ORGANIZER"
        //]

        return roles.stream()
                .filter(role -> role.startsWith("ROLE_"))
                .map(str -> new SimpleGrantedAuthority(str))
                .collect(Collectors.toList());
        // Convert list to a stream and filter out all elements that don't start with ROLE_
        // Converts the role to authorities; SimpleGrantedAuthority("ROLE_ORGANIZER")
    }
}

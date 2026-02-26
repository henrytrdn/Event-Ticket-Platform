package com.example.tickets.filters;

import com.example.tickets.domain.User;
import com.example.tickets.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor // Creates a constructor which will inject this.userRepository via Spring
public class UserProvisioningFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // SecurityContext is Spring Security's current user session storage
        // Spring security validates the JWT, stores it in an Authentication obj and stores it in the SecurityContext

        if(authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof Jwt jwt) {

            UUID keycloakId = UUID.fromString(jwt.getSubject());

            if(!userRepository.existsById(keycloakId)) {
                User user = new User();
                user.setId(keycloakId);
                user.setName(jwt.getClaims().get("preferred_username").toString());
                user.setEmail(jwt.getClaimAsString("email"));
                userRepository.save(user);
            }

        }

        filterChain.doFilter(request, response); // Allows request to continue to the next layer (controller)

    }
}

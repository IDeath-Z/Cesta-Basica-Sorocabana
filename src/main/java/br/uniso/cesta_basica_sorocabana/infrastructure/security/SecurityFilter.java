package br.uniso.cesta_basica_sorocabana.infrastructure.security;

import br.uniso.cesta_basica_sorocabana.domain.ports.TokenProvider;
import br.uniso.cesta_basica_sorocabana.infrastructure.enums.AuthScheme;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// Validates the token straight through the ports while there is no ValidateTokenUseCase yet.
@Slf4j
@Component
@RequiredArgsConstructor
public final class SecurityFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String token = recoverToken(request);
        if (token != null) {
            try {
                String email = tokenProvider.validateAccessToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (userDetails.isEnabled()) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                log.warn("Token validation failed", e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(AuthScheme.BEARER.getHeaderPrefix())) {
            return null;
        } else {
            return authHeader.substring(AuthScheme.BEARER.getHeaderPrefix().length());
        }
    }
}

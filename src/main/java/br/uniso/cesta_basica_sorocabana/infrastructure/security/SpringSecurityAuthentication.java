package br.uniso.cesta_basica_sorocabana.infrastructure.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.infrastructure.enums.errors.AuthError;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class SpringSecurityAuthentication {

    private final AuthenticationManager authenticationManager;

    public User authenticate(String email, String password) {
        var authToken = new UsernamePasswordAuthenticationToken(email, password);
        var authentication = authenticationManager.authenticate(authToken);

        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUserDetails) {
            return ((SecurityUserDetails) principal).getUser();
        }
        throw new IllegalStateException(AuthError.UNEXPECTED_PRINCIPAL.message(principal.getClass())); // Implement better exception handling later
    }
}

package br.uniso.cesta_basica_sorocabana.application.usecases.auth;

import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.ports.TokenProvider;
import br.uniso.cesta_basica_sorocabana.domain.shared.AuthToken;

import lombok.RequiredArgsConstructor;

// Credentials are checked by Spring Security before this runs; here only the access token is issued.
@RequiredArgsConstructor
public final class ProcessUserLoginUseCase {

    private final TokenProvider tokenProvider;

    public AuthToken execute(User user) {
        return new AuthToken(tokenProvider.generateAccessToken(user.getEmail()));
    }
}

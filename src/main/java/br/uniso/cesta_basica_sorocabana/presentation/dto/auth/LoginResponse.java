package br.uniso.cesta_basica_sorocabana.presentation.dto.auth;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.infrastructure.enums.AuthScheme;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse", description = "Access token to send in the Authorization header as 'Bearer <token>'")
public record LoginResponse(
    @Schema(description = "Unique identifier of the logged user", example = "8f7e6d5c-4b3a-4291-8f7e-6d5c4b3a2918")
    UUID userId,

    @Schema(description = "JWT access token. There is no refresh token: log in again when it expires", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken,

    @Schema(description = "Token type", example = "Bearer")
    String tokenType
) {
    public LoginResponse(UUID userId, String accessToken) {
        this(userId, accessToken, AuthScheme.BEARER.getHeaderPrefix().trim());
    }
}

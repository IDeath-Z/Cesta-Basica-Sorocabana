package br.uniso.cesta_basica_sorocabana.presentation.dto.user;

import java.time.Instant;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.user.UserOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserResponse", description = "A system user")
public record UserResponse(
    @Schema(description = "Unique user identifier", example = "8f7e6d5c-4b3a-4291-8f7e-6d5c4b3a2918")
    UUID id,

    @Schema(description = "Login e-mail", example = "maria.silva@uniso.br")
    String email,

    @Schema(description = "ID of the user's role", example = "1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f")
    UUID roleId,

    @Schema(description = "Inactive users cannot log in", example = "true")
    boolean active,

    @Schema(description = "When the user was created", example = "2026-10-12T14:15:36.625Z")
    Instant createdAt
) {
    public static UserResponse from(UserOutput output) {
        return new UserResponse(output.id(), output.email(), output.roleId(), output.active(), output.createdAt());
    }
}

package br.uniso.cesta_basica_sorocabana.presentation.dto.user;

import java.util.Set;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.user.FetchCurrentUserUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CurrentUserResponse", description = "The logged user with the permissions that are in effect right now")
public record CurrentUserResponse(
    @Schema(description = "Unique user identifier", example = "8f7e6d5c-4b3a-4291-8f7e-6d5c4b3a2918")
    UUID id,

    @Schema(description = "Login e-mail", example = "maria.silva@uniso.br")
    String email,

    @Schema(description = "ID of the user's role", example = "1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f")
    UUID roleId,

    @Schema(description = "Name of the user's role", example = "PROFESSOR")
    String roleName,

    @Schema(description = "Permissions in effect; empty when the role is inactive. Use it to show or hide actions in the UI", example = "[\"CREATE_PRODUCT\", \"UPDATE_PRODUCT\"]")
    Set<Permission> permissions
) {
    public static CurrentUserResponse from(FetchCurrentUserUseCase.Output output) {
        return new CurrentUserResponse(output.id(), output.email(), output.roleId(), output.roleName(), output.permissions());
    }
}

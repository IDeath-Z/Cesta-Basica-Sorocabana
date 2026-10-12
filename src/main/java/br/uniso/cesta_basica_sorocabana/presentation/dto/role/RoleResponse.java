package br.uniso.cesta_basica_sorocabana.presentation.dto.role;

import java.util.Set;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.role.RoleOutput;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RoleResponse", description = "A role and the permissions it grants")
public record RoleResponse(
    @Schema(description = "Unique role identifier", example = "1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f")
    UUID id,

    @Schema(description = "Role name", example = "PROFESSOR")
    String name,

    @Schema(description = "Inactive roles grant no permission", example = "true")
    boolean active,

    @Schema(description = "Permissions granted by the role", example = "[\"CREATE_PRODUCT\", \"UPDATE_PRODUCT\"]")
    Set<Permission> permissions
) {
    public static RoleResponse from(RoleOutput output) {
        return new RoleResponse(output.id(), output.name(), output.active(), output.permissions());
    }
}

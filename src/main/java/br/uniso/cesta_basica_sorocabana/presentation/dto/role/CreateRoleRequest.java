package br.uniso.cesta_basica_sorocabana.presentation.dto.role;

import java.util.Set;

import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateRoleRequest", description = "Payload for creating a role. A role only groups permissions; access is always checked by permission")
public record CreateRoleRequest(
    @NotBlank @Size(max = 60) @Schema(description = "Role name, must be unique", example = "PROFESSOR")
    String name,

    @NotNull @Schema(description = "Permissions granted by the role. See GET /api/v1/roles/permissions for the full list", example = "[\"CREATE_PRODUCT\", \"UPDATE_PRODUCT\"]")
    Set<Permission> permissions
) {}

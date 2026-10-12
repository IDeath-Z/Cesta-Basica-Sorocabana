package br.uniso.cesta_basica_sorocabana.presentation.dto.role;

import java.util.Set;

import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateRoleRequest", description = "Payload for updating a role. Omitted (null) fields keep their current value")
public record UpdateRoleRequest(
    @Size(min = 1, max = 60) @Schema(description = "Role name, must be unique", example = "PROFESSOR")
    String name,

    @Schema(description = "Replaces the whole set of permissions when provided. Send [] to remove all", example = "[\"CREATE_PRODUCT\", \"UPDATE_PRODUCT\", \"CREATE_BRAND\"]")
    Set<Permission> permissions
) {}

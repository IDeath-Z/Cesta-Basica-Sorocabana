package br.uniso.cesta_basica_sorocabana.presentation.dto.user;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ChangeUserRoleRequest", description = "Payload for moving a user to another role")
public record ChangeUserRoleRequest(
    @NotNull @Schema(description = "ID of an active role", example = "1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f")
    UUID roleId
) {}

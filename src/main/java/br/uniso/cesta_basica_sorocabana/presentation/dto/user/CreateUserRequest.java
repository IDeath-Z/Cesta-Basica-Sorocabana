package br.uniso.cesta_basica_sorocabana.presentation.dto.user;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateUserRequest", description = "Payload for creating a user")
public record CreateUserRequest(
    @NotBlank @Email @Size(max = 254) @Schema(description = "Login e-mail, unique and stored in lowercase", example = "maria.silva@uniso.br", format = "email")
    String email,

    @NotBlank @Size(min = 8, max = 72) @Schema(description = "Initial password, 8 to 72 characters. The user can change it later", example = "Initial@2026")
    String password,

    @NotNull @Schema(description = "ID of an active role, which defines the user's permissions", example = "1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f")
    UUID roleId
) {}

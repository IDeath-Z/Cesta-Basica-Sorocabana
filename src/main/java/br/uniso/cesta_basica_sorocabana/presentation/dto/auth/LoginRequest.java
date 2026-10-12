package br.uniso.cesta_basica_sorocabana.presentation.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequest", description = "User credentials")
public record LoginRequest(
    @NotBlank @Email @Schema(description = "Login e-mail", example = "admin@uniso.br", format = "email")
    String email,

    @NotBlank @Schema(description = "Plain-text password", example = "Initial@2026")
    String password
) {}

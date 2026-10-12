package br.uniso.cesta_basica_sorocabana.presentation.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "ChangePasswordRequest", description = "Payload for changing the logged user's own password")
public record ChangePasswordRequest(
    @NotBlank @Schema(description = "Current password", example = "Initial@2026")
    String currentPassword,

    @NotBlank @Size(min = 8, max = 72) @Schema(description = "New password, 8 to 72 characters", example = "MyNewPassword@2026")
    String newPassword
) {}

package br.uniso.cesta_basica_sorocabana.presentation.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "ResetPasswordRequest", description = "Payload for setting a new password for another user")
public record ResetPasswordRequest(
    @NotBlank @Size(min = 8, max = 72) @Schema(description = "New password, 8 to 72 characters. Tell the user to change it after logging in", example = "Temporary@2026")
    String newPassword
) {}

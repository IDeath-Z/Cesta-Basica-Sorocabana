package br.uniso.cesta_basica_sorocabana.presentation.dto.exceptions;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiErrorResponse", description = "Standard error body returned by every failed request")
public record ApiErrorResponse(
    @Schema(description = "When the error happened", example = "2026-10-12T14:15:36.625Z")
    Instant timestamp,

    @Schema(description = "HTTP status code", example = "404")
    Integer status,

    @Schema(description = "HTTP status reason", example = "Not Found")
    String error,

    @Schema(description = "Human readable error message", example = "Marca 3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f não encontrada.")
    String message,

    @Schema(description = "Requested path", example = "/api/v1/brands/3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
    String path
) {}

package br.uniso.cesta_basica_sorocabana.presentation.dto.audit;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonRawValue;

import br.uniso.cesta_basica_sorocabana.application.usecases.audit.AuditOutput;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditAction;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuditResponse", description = "A change made by a user")
public record AuditResponse(
    @Schema(description = "Unique audit record identifier", example = "b1a2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d")
    UUID id,

    @Schema(description = "User who made the change", example = "8f7e6d5c-4b3a-4291-8f7e-6d5c4b3a2918")
    UUID userId,

    @Schema(description = "Kind of change", example = "UPDATE")
    AuditAction action,

    @Schema(description = "Changed entity", example = "PRODUCT")
    AuditedEntity entity,

    @Schema(description = "ID of the changed record", example = "9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a")
    UUID entityId,

    @JsonRawValue
    @Schema(description = "Record before the change, as JSON. Null on INSERT", type = "object", example = "{\"id\": \"9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a\", \"name\": \"Arroz\", \"active\": true}")
    String dataBefore,

    @JsonRawValue
    @Schema(description = "Record after the change, as JSON. Null on DELETE", type = "object", example = "{\"id\": \"9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a\", \"name\": \"Arroz Integral\", \"active\": true}")
    String dataAfter,

    @Schema(description = "When the change happened", example = "2026-10-12T14:15:36.625Z")
    Instant createdAt
) {
    public static AuditResponse from(AuditOutput output) {
        return new AuditResponse(output.id(), output.userId(), output.action(), output.entity(), output.entityId(),
            output.dataBefore(), output.dataAfter(), output.createdAt());
    }
}

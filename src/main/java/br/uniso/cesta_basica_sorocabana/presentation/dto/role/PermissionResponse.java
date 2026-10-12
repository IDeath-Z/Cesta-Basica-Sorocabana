package br.uniso.cesta_basica_sorocabana.presentation.dto.role;

import br.uniso.cesta_basica_sorocabana.application.usecases.role.PermissionOutput;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PermissionResponse", description = "A permission defined in the code")
public record PermissionResponse(
    @Schema(description = "Permission code, used in role payloads", example = "CREATE_PRODUCT")
    Permission code,

    @Schema(description = "What the permission allows", example = "Cadastrar produto")
    String description
) {
    public static PermissionResponse from(PermissionOutput output) {
        return new PermissionResponse(output.code(), output.description());
    }
}

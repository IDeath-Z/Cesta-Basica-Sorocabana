package br.uniso.cesta_basica_sorocabana.presentation.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.uniso.cesta_basica_sorocabana.application.usecases.audit.FetchAuditUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.audit.FetchAuditsUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.enums.SortBy;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.presentation.dto.audit.AuditResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.shared.PageResponse;
import br.uniso.cesta_basica_sorocabana.presentation.enums.AuditEntityFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audits")
@RequiresPermission(Permission.VIEW_AUDIT)
@Tag(name = "Audit", description = "Read-only history of what each user changed. Every route requires permission VIEW_AUDIT.")
public class AuditController {

    private final FetchAuditUseCase fetchAuditUseCase;
    private final FetchAuditsUseCase fetchAuditsUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping
    @Operation(summary = "List audit records", description = """
        Returns a page of audit records, newest first by default. Every filter is optional and they combine:
        entity=ALL (default) lists every entity, entity=BRAND lists the history of all brands,
        entity=BRAND&entityId=... the history of one brand, and userId narrows any of these to what one user did.
        Valid sortBy values: CREATED_AT_ASC, CREATED_AT_DESC. Requires permission VIEW_AUDIT.""")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of audit records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter, page, size or sortBy")
    })
    public ResponseEntity<PageResponse<AuditResponse>> list(
            @Parameter(description = "Entity whose history is listed, ALL for every entity", example = "BRAND") @RequestParam(defaultValue = "ALL") AuditEntityFilter entity,
            @Parameter(description = "Only the history of this record, optional", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @RequestParam(required = false) UUID entityId,
            @Parameter(description = "Only changes made by this user, optional", example = "8f7e6d5c-4b3a-4291-8f7e-6d5c4b3a2918") @RequestParam(required = false) UUID userId,
            @Parameter(description = "Page number, starting at 0", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Items per page, from 1 to 100", example = "20") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort order", example = "CREATED_AT_DESC") @RequestParam(defaultValue = "CREATED_AT_DESC") SortBy sortBy) {

        var output = transactionalProxy.executeReadOnly(() -> fetchAuditsUseCase.execute(
            new FetchAuditsUseCase.Input(userId, entity.toAuditedEntity(), entityId, new DomainPageRequest(page, size, sortBy))
        ));

        return ResponseEntity.ok(PageResponse.from(output, AuditResponse::from));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an audit record", description = "Returns a single audit record with the data before and after the change. Requires permission VIEW_AUDIT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Audit record found"),
        @ApiResponse(responseCode = "404", description = "Audit record not found")
    })
    public ResponseEntity<AuditResponse> get(
            @Parameter(description = "Audit record ID", example = "b1a2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchAuditUseCase.execute(new FetchAuditUseCase.Input(id)));

        return ResponseEntity.ok(AuditResponse.from(output));
    }
}

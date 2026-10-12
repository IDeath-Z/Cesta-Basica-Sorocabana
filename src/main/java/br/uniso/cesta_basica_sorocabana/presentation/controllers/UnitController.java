package br.uniso.cesta_basica_sorocabana.presentation.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.uniso.cesta_basica_sorocabana.application.usecases.unit.UpdateUnitStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.unit.CreateUnitUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.unit.FetchUnitUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.unit.FetchUnitsUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.unit.UpdateUnitUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.enums.SortBy;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityUserDetails;
import br.uniso.cesta_basica_sorocabana.presentation.dto.unit.UnitResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.unit.CreateUnitRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.unit.UpdateUnitRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.shared.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/units")
@Tag(name = "Units", description = "Sales units of products (e.g. 5 kg, 200 g, 8 un). Reading requires only a valid token; writing requires the permission listed on each route.")
public class UnitController {

    private final CreateUnitUseCase createUnitUseCase;
    private final UpdateUnitUseCase updateUnitUseCase;
    private final FetchUnitUseCase fetchUnitUseCase;
    private final FetchUnitsUseCase fetchUnitsUseCase;
    private final UpdateUnitStatusUseCase updateUnitStatusUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping
    @Operation(summary = "List units", description = "Returns a page of units, active and inactive. Valid sortBy values: MEASURE_ASC, MEASURE_DESC.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of units"),
        @ApiResponse(responseCode = "400", description = "Invalid page, size or sortBy")
    })
    public ResponseEntity<PageResponse<UnitResponse>> list(
            @Parameter(description = "Page number, starting at 0", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Items per page, from 1 to 100", example = "20") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort order", example = "MEASURE_ASC") @RequestParam(defaultValue = "MEASURE_ASC") SortBy sortBy) {

        var output = transactionalProxy.executeReadOnly(() -> fetchUnitsUseCase.execute(
            new FetchUnitsUseCase.Input(new DomainPageRequest(page, size, sortBy))
        ));

        return ResponseEntity.ok(PageResponse.from(output, UnitResponse::from));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a unit", description = "Returns a single unit by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Unit found"),
        @ApiResponse(responseCode = "404", description = "Unit not found")
    })
    public ResponseEntity<UnitResponse> get(
            @Parameter(description = "Unit ID", example = "7a9d4e21-3c5b-4f8a-b6d2-9e1f0a3b5c7d") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchUnitUseCase.execute(new FetchUnitUseCase.Input(id)));

        return ResponseEntity.ok(UnitResponse.from(output));
    }

    @PostMapping
    @RequiresPermission(Permission.CREATE_UNIT)
    @Operation(summary = "Create a unit", description = "Registers a new active unit. Requires permission CREATE_UNIT.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Unit created"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "409", description = "A unit with this quantity and measure already exists")
    })
    public ResponseEntity<UnitResponse> create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateUnitRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createUnitUseCase.execute(new CreateUnitUseCase.Input(requesterId, request.quantity(), request.measure())));

        return ResponseEntity.status(HttpStatus.CREATED).body(UnitResponse.from(output));
    }

    @PatchMapping("/{id}")
    @RequiresPermission(Permission.UPDATE_UNIT)
    @Operation(summary = "Update a unit", description = "Changes the quantity and/or measure of an existing unit. Omitted fields keep their value. Requires permission UPDATE_UNIT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Unit updated"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "404", description = "Unit not found"),
        @ApiResponse(responseCode = "409", description = "Another unit already has this quantity and measure")
    })
    public ResponseEntity<UnitResponse> update(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Unit ID", example = "7a9d4e21-3c5b-4f8a-b6d2-9e1f0a3b5c7d") @PathVariable UUID id,
            @RequestBody @Valid UpdateUnitRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateUnitUseCase.execute(new UpdateUnitUseCase.Input(requesterId, id, request.quantity(), request.measure())));

        return ResponseEntity.ok(UnitResponse.from(output));
    }

    @PatchMapping("/{id}/activate")
    @RequiresPermission(Permission.UPDATE_UNIT)
    @Operation(summary = "Activate a unit", description = "Marks the unit as active again. Does nothing if it is already active. Requires permission UPDATE_UNIT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Unit is active"),
        @ApiResponse(responseCode = "404", description = "Unit not found")
    })
    public ResponseEntity<UnitResponse> activate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Unit ID", example = "7a9d4e21-3c5b-4f8a-b6d2-9e1f0a3b5c7d") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateUnitStatusUseCase.execute(new UpdateUnitStatusUseCase.Input(requesterId, id, true)));

        return ResponseEntity.ok(UnitResponse.from(output));
    }

    @PatchMapping("/{id}/deactivate")
    @RequiresPermission(Permission.UPDATE_UNIT)
    @Operation(summary = "Deactivate a unit", description = "Marks the unit as inactive so it can no longer be used by new products. Existing records keep it. Requires permission UPDATE_UNIT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Unit is inactive"),
        @ApiResponse(responseCode = "404", description = "Unit not found")
    })
    public ResponseEntity<UnitResponse> deactivate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Unit ID", example = "7a9d4e21-3c5b-4f8a-b6d2-9e1f0a3b5c7d") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateUnitStatusUseCase.execute(new UpdateUnitStatusUseCase.Input(requesterId, id, false)));

        return ResponseEntity.ok(UnitResponse.from(output));
    }
}

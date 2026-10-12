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

import br.uniso.cesta_basica_sorocabana.application.usecases.store.UpdateStoreStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.store.CreateStoreUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.store.FetchStoreUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.store.FetchStoresUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.store.UpdateStoreUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.enums.SortBy;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityUserDetails;
import br.uniso.cesta_basica_sorocabana.presentation.dto.store.StoreResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.store.CreateStoreRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.store.UpdateStoreRequest;
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
@RequestMapping("/stores")
@Tag(name = "Stores", description = "Product stores (e.g. Camil, Pilecco). Reading requires only a valid token; writing requires the permission listed on each route.")
public class StoreController {

    private final CreateStoreUseCase createStoreUseCase;
    private final UpdateStoreUseCase updateStoreUseCase;
    private final FetchStoreUseCase fetchStoreUseCase;
    private final FetchStoresUseCase fetchStoresUseCase;
    private final UpdateStoreStatusUseCase updateStoreStatusUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping
    @Operation(summary = "List stores", description = "Returns a page of stores, active and inactive. Valid sortBy values: NAME_ASC, NAME_DESC.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of stores"),
        @ApiResponse(responseCode = "400", description = "Invalid page, size or sortBy")
    })
    public ResponseEntity<PageResponse<StoreResponse>> list(
            @Parameter(description = "Page number, starting at 0", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Items per page, from 1 to 100", example = "20") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort order", example = "NAME_ASC") @RequestParam(defaultValue = "NAME_ASC") SortBy sortBy) {

        var output = transactionalProxy.executeReadOnly(() -> fetchStoresUseCase.execute(
            new FetchStoresUseCase.Input(new DomainPageRequest(page, size, sortBy))
        ));

        return ResponseEntity.ok(PageResponse.from(output, StoreResponse::from));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a store", description = "Returns a single store by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Store found"),
        @ApiResponse(responseCode = "404", description = "Store not found")
    })
    public ResponseEntity<StoreResponse> get(
            @Parameter(description = "Store ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchStoreUseCase.execute(new FetchStoreUseCase.Input(id)));

        return ResponseEntity.ok(StoreResponse.from(output));
    }

    @PostMapping
    @RequiresPermission(Permission.CREATE_STORE)
    @Operation(summary = "Create a store", description = "Registers a new active store. Requires permission CREATE_STORE.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Store created"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "409", description = "A store with this name already exists")
    })
    public ResponseEntity<StoreResponse> create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateStoreRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createStoreUseCase.execute(new CreateStoreUseCase.Input(requesterId, request.name())));

        return ResponseEntity.status(HttpStatus.CREATED).body(StoreResponse.from(output));
    }

    @PatchMapping("/{id}")
    @RequiresPermission(Permission.UPDATE_STORE)
    @Operation(summary = "Rename a store", description = "Changes the name of an existing store. Requires permission UPDATE_STORE.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Store updated"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "404", description = "Store not found"),
        @ApiResponse(responseCode = "409", description = "Another store already has this name")
    })
    public ResponseEntity<StoreResponse> update(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Store ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id,
            @RequestBody @Valid UpdateStoreRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateStoreUseCase.execute(new UpdateStoreUseCase.Input(requesterId, id, request.name())));

        return ResponseEntity.ok(StoreResponse.from(output));
    }

    @PatchMapping("/{id}/activate")
    @RequiresPermission(Permission.UPDATE_STORE)
    @Operation(summary = "Activate a store", description = "Marks the store as active again. Does nothing if it is already active. Requires permission UPDATE_STORE.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Store is active"),
        @ApiResponse(responseCode = "404", description = "Store not found")
    })
    public ResponseEntity<StoreResponse> activate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Store ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateStoreStatusUseCase.execute(new UpdateStoreStatusUseCase.Input(requesterId, id, true)));

        return ResponseEntity.ok(StoreResponse.from(output));
    }

    @PatchMapping("/{id}/deactivate")
    @RequiresPermission(Permission.UPDATE_STORE)
    @Operation(summary = "Deactivate a store", description = "Marks the store as inactive so it is no longer offered in new price surveys. Existing records keep it. Requires permission UPDATE_STORE.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Store is inactive"),
        @ApiResponse(responseCode = "404", description = "Store not found")
    })
    public ResponseEntity<StoreResponse> deactivate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Store ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateStoreStatusUseCase.execute(new UpdateStoreStatusUseCase.Input(requesterId, id, false)));

        return ResponseEntity.ok(StoreResponse.from(output));
    }
}

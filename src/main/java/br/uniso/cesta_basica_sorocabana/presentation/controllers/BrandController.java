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

import br.uniso.cesta_basica_sorocabana.application.usecases.brand.UpdateBrandStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.brand.CreateBrandUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.brand.FetchBrandUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.brand.FetchBrandsUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.brand.UpdateBrandUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.enums.SortBy;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityUserDetails;
import br.uniso.cesta_basica_sorocabana.presentation.dto.brand.BrandResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.brand.CreateBrandRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.brand.UpdateBrandRequest;
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
@RequestMapping("/brands")
@Tag(name = "Brands", description = "Product brands (e.g. Camil, Pilecco). Reading requires only a valid token; writing requires the permission listed on each route.")
public class BrandController {

    private final CreateBrandUseCase createBrandUseCase;
    private final UpdateBrandUseCase updateBrandUseCase;
    private final FetchBrandUseCase fetchBrandUseCase;
    private final FetchBrandsUseCase fetchBrandsUseCase;
    private final UpdateBrandStatusUseCase updateBrandStatusUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping
    @Operation(summary = "List brands", description = "Returns a page of brands, active and inactive. Valid sortBy values: NAME_ASC, NAME_DESC.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of brands"),
        @ApiResponse(responseCode = "400", description = "Invalid page, size or sortBy")
    })
    public ResponseEntity<PageResponse<BrandResponse>> list(
            @Parameter(description = "Page number, starting at 0", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Items per page, from 1 to 100", example = "20") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort order", example = "NAME_ASC") @RequestParam(defaultValue = "NAME_ASC") SortBy sortBy) {

        var output = transactionalProxy.executeReadOnly(() -> fetchBrandsUseCase.execute(
            new FetchBrandsUseCase.Input(new DomainPageRequest(page, size, sortBy))
        ));

        return ResponseEntity.ok(PageResponse.from(output, BrandResponse::from));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a brand", description = "Returns a single brand by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Brand found"),
        @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<BrandResponse> get(
            @Parameter(description = "Brand ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchBrandUseCase.execute(new FetchBrandUseCase.Input(id)));

        return ResponseEntity.ok(BrandResponse.from(output));
    }

    @PostMapping
    @RequiresPermission(Permission.CREATE_BRAND)
    @Operation(summary = "Create a brand", description = "Registers a new active brand. Requires permission CREATE_BRAND.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Brand created"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "409", description = "A brand with this name already exists")
    })
    public ResponseEntity<BrandResponse> create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateBrandRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createBrandUseCase.execute(new CreateBrandUseCase.Input(requesterId, request.name())));

        return ResponseEntity.status(HttpStatus.CREATED).body(BrandResponse.from(output));
    }

    @PatchMapping("/{id}")
    @RequiresPermission(Permission.UPDATE_BRAND)
    @Operation(summary = "Rename a brand", description = "Changes the name of an existing brand. Requires permission UPDATE_BRAND.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Brand updated"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "404", description = "Brand not found"),
        @ApiResponse(responseCode = "409", description = "Another brand already has this name")
    })
    public ResponseEntity<BrandResponse> update(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Brand ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id,
            @RequestBody @Valid UpdateBrandRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateBrandUseCase.execute(new UpdateBrandUseCase.Input(requesterId, id, request.name())));

        return ResponseEntity.ok(BrandResponse.from(output));
    }

    @PatchMapping("/{id}/activate")
    @RequiresPermission(Permission.UPDATE_BRAND)
    @Operation(summary = "Activate a brand", description = "Marks the brand as active again. Does nothing if it is already active. Requires permission UPDATE_BRAND.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Brand is active"),
        @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<BrandResponse> activate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Brand ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateBrandStatusUseCase.execute(new UpdateBrandStatusUseCase.Input(requesterId, id, true)));

        return ResponseEntity.ok(BrandResponse.from(output));
    }

    @PatchMapping("/{id}/deactivate")
    @RequiresPermission(Permission.UPDATE_BRAND)
    @Operation(summary = "Deactivate a brand", description = "Marks the brand as inactive so it can no longer be linked to products. Existing records keep it. Requires permission UPDATE_BRAND.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Brand is inactive"),
        @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<BrandResponse> deactivate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Brand ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateBrandStatusUseCase.execute(new UpdateBrandStatusUseCase.Input(requesterId, id, false)));

        return ResponseEntity.ok(BrandResponse.from(output));
    }
}

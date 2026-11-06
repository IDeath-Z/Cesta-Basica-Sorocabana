package br.uniso.cesta_basica_sorocabana.presentation.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore.CreateProductBrandStoreUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore.FetchProductBrandStoreUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore.FetchProductBrandStoresUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore.UpdateProductBrandStoreStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore.UpdateProductBrandStoreUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityUserDetails;
import br.uniso.cesta_basica_sorocabana.presentation.dto.productbrandstore.CreateProductBrandStoreRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.productbrandstore.ProductBrandStoreResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.productbrandstore.UpdateProductBrandStoreRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product Brand Stores", description = "Links between a basket item (product + brand) and a store, with the name the store uses for it. Without the link the item is not surveyed in that store. Reading requires only a valid token; writing requires the permission listed on each route.")
public class ProductBrandStoreController {

    private final CreateProductBrandStoreUseCase createProductBrandStoreUseCase;
    private final UpdateProductBrandStoreUseCase updateProductBrandStoreUseCase;
    private final UpdateProductBrandStoreStatusUseCase updateProductBrandStoreStatusUseCase;
    private final FetchProductBrandStoreUseCase fetchProductBrandStoreUseCase;
    private final FetchProductBrandStoresUseCase fetchProductBrandStoresUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping("/product-brands/{productBrandId}/stores")
    @Operation(summary = "List the stores of a basket item", description = "Returns every store linked to the product + brand, active and inactive.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stores linked to the item"),
        @ApiResponse(responseCode = "404", description = "Product brand not found")
    })
    public ResponseEntity<List<ProductBrandStoreResponse>> listByProductBrand(
            @Parameter(description = "Product brand ID", example = "e4d3c2b1-a0f9-4e8d-7c6b-5a4f3e2d1c0b") @PathVariable UUID productBrandId) {

        var output = transactionalProxy.executeReadOnly(() -> fetchProductBrandStoresUseCase.execute(
            new FetchProductBrandStoresUseCase.Input(productBrandId)
        ));

        return ResponseEntity.ok(output.stream().map(ProductBrandStoreResponse::from).toList());
    }

    @PostMapping("/product-brands/{productBrandId}/stores")
    @RequiresPermission(Permission.CREATE_PRODUCT_BRAND_STORE)
    @Operation(summary = "Link a basket item to a store", description = "Creates the item + store link with its search name. Both must exist and be active, and the link must not exist yet. Requires permission CREATE_PRODUCT_BRAND_STORE.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Item linked to the store"),
        @ApiResponse(responseCode = "400", description = "Invalid payload, or the item or store is inactive"),
        @ApiResponse(responseCode = "404", description = "Product brand or store not found"),
        @ApiResponse(responseCode = "409", description = "The item is already linked to this store")
    })
    public ResponseEntity<ProductBrandStoreResponse> create(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Product brand ID", example = "e4d3c2b1-a0f9-4e8d-7c6b-5a4f3e2d1c0b") @PathVariable UUID productBrandId,
            @RequestBody @Valid CreateProductBrandStoreRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createProductBrandStoreUseCase.execute(
            new CreateProductBrandStoreUseCase.Input(requesterId, productBrandId, request.storeId(), request.searchName())
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(ProductBrandStoreResponse.from(output));
    }

    @GetMapping("/product-brand-stores/{id}")
    @Operation(summary = "Get a link", description = "Returns a single item + store link by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Link found"),
        @ApiResponse(responseCode = "404", description = "Link not found")
    })
    public ResponseEntity<ProductBrandStoreResponse> get(
            @Parameter(description = "Link ID", example = "b7a6c5d4-e3f2-4a1b-9c8d-7e6f5a4b3c2d") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchProductBrandStoreUseCase.execute(new FetchProductBrandStoreUseCase.Input(id)));

        return ResponseEntity.ok(ProductBrandStoreResponse.from(output));
    }

    @PatchMapping("/product-brand-stores/{id}")
    @RequiresPermission(Permission.UPDATE_PRODUCT_BRAND_STORE)
    @Operation(summary = "Change the search name", description = "Changes how the store names the item. Requires permission UPDATE_PRODUCT_BRAND_STORE.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Link updated"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "404", description = "Link not found")
    })
    public ResponseEntity<ProductBrandStoreResponse> update(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Link ID", example = "b7a6c5d4-e3f2-4a1b-9c8d-7e6f5a4b3c2d") @PathVariable UUID id,
            @RequestBody @Valid UpdateProductBrandStoreRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateProductBrandStoreUseCase.execute(
            new UpdateProductBrandStoreUseCase.Input(requesterId, id, request.searchName())
        ));

        return ResponseEntity.ok(ProductBrandStoreResponse.from(output));
    }

    @PatchMapping("/product-brand-stores/{id}/activate")
    @RequiresPermission(Permission.UPDATE_PRODUCT_BRAND_STORE)
    @Operation(summary = "Activate a link", description = "Marks the link as active again. Does nothing if it is already active. Requires permission UPDATE_PRODUCT_BRAND_STORE.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Link is active"),
        @ApiResponse(responseCode = "404", description = "Link not found")
    })
    public ResponseEntity<ProductBrandStoreResponse> activate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Link ID", example = "b7a6c5d4-e3f2-4a1b-9c8d-7e6f5a4b3c2d") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateProductBrandStoreStatusUseCase.execute(
            new UpdateProductBrandStoreStatusUseCase.Input(requesterId, id, true)
        ));

        return ResponseEntity.ok(ProductBrandStoreResponse.from(output));
    }

    @PatchMapping("/product-brand-stores/{id}/deactivate")
    @RequiresPermission(Permission.UPDATE_PRODUCT_BRAND_STORE)
    @Operation(summary = "Deactivate a link", description = "Marks the link as inactive so the item is no longer surveyed in that store. Requires permission UPDATE_PRODUCT_BRAND_STORE.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Link is inactive"),
        @ApiResponse(responseCode = "404", description = "Link not found")
    })
    public ResponseEntity<ProductBrandStoreResponse> deactivate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Link ID", example = "b7a6c5d4-e3f2-4a1b-9c8d-7e6f5a4b3c2d") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateProductBrandStoreStatusUseCase.execute(
            new UpdateProductBrandStoreStatusUseCase.Input(requesterId, id, false)
        ));

        return ResponseEntity.ok(ProductBrandStoreResponse.from(output));
    }
}

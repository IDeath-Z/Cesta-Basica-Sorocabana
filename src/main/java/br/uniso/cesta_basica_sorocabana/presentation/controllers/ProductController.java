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

import br.uniso.cesta_basica_sorocabana.application.usecases.product.UpdateProductStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.product.CreateProductUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.product.FetchProductUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.product.FetchProductsUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.product.UpdateProductUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.enums.SortBy;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityUserDetails;
import br.uniso.cesta_basica_sorocabana.presentation.dto.product.ProductResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.product.CreateProductRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.product.UpdateProductRequest;
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
@RequestMapping("/products")
@Tag(name = "Products", description = "Products of the basic food basket (e.g. Arroz, Creme Dental). Reading requires only a valid token; writing requires the permission listed on each route.")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final FetchProductUseCase fetchProductUseCase;
    private final FetchProductsUseCase fetchProductsUseCase;
    private final UpdateProductStatusUseCase updateProductStatusUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping
    @Operation(summary = "List products", description = "Returns a page of products, active and inactive, optionally only from one category. Valid sortBy values: NAME_ASC, NAME_DESC.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of products"),
        @ApiResponse(responseCode = "400", description = "Invalid page, size or sortBy")
    })
    public ResponseEntity<PageResponse<ProductResponse>> list(
            @Parameter(description = "Only products of this category, optional", example = "5b2e7c1d-9f4a-4d3b-8e6c-2a1f0b9c8d7e") @RequestParam(required = false) UUID categoryId,
            @Parameter(description = "Page number, starting at 0", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Items per page, from 1 to 100", example = "20") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort order", example = "NAME_ASC") @RequestParam(defaultValue = "NAME_ASC") SortBy sortBy) {

        var output = transactionalProxy.executeReadOnly(() -> fetchProductsUseCase.execute(
            new FetchProductsUseCase.Input(categoryId, new DomainPageRequest(page, size, sortBy))
        ));

        return ResponseEntity.ok(PageResponse.from(output, ProductResponse::from));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product", description = "Returns a single product by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> get(
            @Parameter(description = "Product ID", example = "9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchProductUseCase.execute(new FetchProductUseCase.Input(id)));

        return ResponseEntity.ok(ProductResponse.from(output));
    }

    @PostMapping
    @RequiresPermission(Permission.CREATE_PRODUCT)
    @Operation(summary = "Create a product", description = "Registers a new active product. The category and the unit (when given) must exist and be active. Requires permission CREATE_PRODUCT.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Product created"),
        @ApiResponse(responseCode = "400", description = "Invalid payload, or the category or unit is inactive"),
        @ApiResponse(responseCode = "404", description = "Category or unit not found"),
        @ApiResponse(responseCode = "409", description = "A product with this name and specification already exists")
    })
    public ResponseEntity<ProductResponse> create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateProductRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createProductUseCase.execute(new CreateProductUseCase.Input(
            requesterId,
            request.categoryId(),
            request.unitId(),
            request.name(),
            request.specification(),
            request.notes()
        )));

        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(output));
    }

    @PatchMapping("/{id}")
    @RequiresPermission(Permission.UPDATE_PRODUCT)
    @Operation(summary = "Update a product", description = "Changes the fields of an existing product. Omitted fields keep their value; a new category or unit must exist and be active. Requires permission UPDATE_PRODUCT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated"),
        @ApiResponse(responseCode = "400", description = "Invalid payload, or the new category or unit is inactive"),
        @ApiResponse(responseCode = "404", description = "Product, category or unit not found"),
        @ApiResponse(responseCode = "409", description = "Another product already has this name and specification")
    })
    public ResponseEntity<ProductResponse> update(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Product ID", example = "9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a") @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateProductUseCase.execute(new UpdateProductUseCase.Input(
            requesterId,
            id,
            request.categoryId(),
            request.unitId(),
            request.name(),
            request.specification(),
            request.notes()
        )));

        return ResponseEntity.ok(ProductResponse.from(output));
    }

    @PatchMapping("/{id}/activate")
    @RequiresPermission(Permission.UPDATE_PRODUCT)
    @Operation(summary = "Activate a product", description = "Marks the product as active again. Does nothing if it is already active. Requires permission UPDATE_PRODUCT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product is active"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> activate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Product ID", example = "9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateProductStatusUseCase.execute(new UpdateProductStatusUseCase.Input(requesterId, id, true)));

        return ResponseEntity.ok(ProductResponse.from(output));
    }

    @PatchMapping("/{id}/deactivate")
    @RequiresPermission(Permission.UPDATE_PRODUCT)
    @Operation(summary = "Deactivate a product", description = "Marks the product as inactive so it can no longer get new brands or enter new surveys. Existing records keep it. Requires permission UPDATE_PRODUCT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product is inactive"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> deactivate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Product ID", example = "9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateProductStatusUseCase.execute(new UpdateProductStatusUseCase.Input(requesterId, id, false)));

        return ResponseEntity.ok(ProductResponse.from(output));
    }
}

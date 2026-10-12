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

import br.uniso.cesta_basica_sorocabana.application.usecases.productbrand.UpdateProductBrandStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrand.CreateProductBrandUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrand.FetchProductBrandUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrand.FetchProductBrandsUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityUserDetails;
import br.uniso.cesta_basica_sorocabana.presentation.dto.productbrand.CreateProductBrandRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.productbrand.ProductBrandResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product Brands", description = "Links between a product and a brand (e.g. Arroz + Camil). Each pair is what gets priced in a survey. Reading requires only a valid token; writing requires the permission listed on each route.")
public class ProductBrandController {

    private final CreateProductBrandUseCase createProductBrandUseCase;
    private final FetchProductBrandUseCase fetchProductBrandUseCase;
    private final FetchProductBrandsUseCase fetchProductBrandsUseCase;
    private final UpdateProductBrandStatusUseCase updateProductBrandStatusUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping("/products/{productId}/brands")
    @Operation(summary = "List the brands of a product", description = "Returns every brand linked to the product, active and inactive.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Brands linked to the product"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<List<ProductBrandResponse>> listByProduct(
            @Parameter(description = "Product ID", example = "9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a") @PathVariable UUID productId) {

        var output = transactionalProxy.executeReadOnly(() -> fetchProductBrandsUseCase.execute(
            new FetchProductBrandsUseCase.Input(productId)
        ));

        return ResponseEntity.ok(output.stream().map(ProductBrandResponse::from).toList());
    }

    @PostMapping("/products/{productId}/brands")
    @RequiresPermission(Permission.CREATE_PRODUCT_BRAND)
    @Operation(summary = "Link a brand to a product", description = "Creates the product + brand pair. Both must exist and be active, and the pair must not exist yet. Requires permission CREATE_PRODUCT_BRAND.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Brand linked to the product"),
        @ApiResponse(responseCode = "400", description = "Invalid payload, or the product or brand is inactive"),
        @ApiResponse(responseCode = "404", description = "Product or brand not found"),
        @ApiResponse(responseCode = "409", description = "The brand is already linked to this product")
    })
    public ResponseEntity<ProductBrandResponse> create(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Product ID", example = "9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a") @PathVariable UUID productId,
            @RequestBody @Valid CreateProductBrandRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createProductBrandUseCase.execute(
            new CreateProductBrandUseCase.Input(requesterId, productId, request.brandId())
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(ProductBrandResponse.from(output));
    }

    @GetMapping("/product-brands/{id}")
    @Operation(summary = "Get a product brand", description = "Returns a single product + brand pair by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product brand found"),
        @ApiResponse(responseCode = "404", description = "Product brand not found")
    })
    public ResponseEntity<ProductBrandResponse> get(
            @Parameter(description = "Product brand ID", example = "e4d3c2b1-a0f9-4e8d-7c6b-5a4f3e2d1c0b") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchProductBrandUseCase.execute(new FetchProductBrandUseCase.Input(id)));

        return ResponseEntity.ok(ProductBrandResponse.from(output));
    }

    @PatchMapping("/product-brands/{id}/activate")
    @RequiresPermission(Permission.UPDATE_PRODUCT_BRAND)
    @Operation(summary = "Activate a product brand", description = "Marks the pair as active again. Does nothing if it is already active. Requires permission UPDATE_PRODUCT_BRAND.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product brand is active"),
        @ApiResponse(responseCode = "404", description = "Product brand not found")
    })
    public ResponseEntity<ProductBrandResponse> activate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Product brand ID", example = "e4d3c2b1-a0f9-4e8d-7c6b-5a4f3e2d1c0b") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateProductBrandStatusUseCase.execute(
            new UpdateProductBrandStatusUseCase.Input(requesterId, id, true)
        ));

        return ResponseEntity.ok(ProductBrandResponse.from(output));
    }

    @PatchMapping("/product-brands/{id}/deactivate")
    @RequiresPermission(Permission.UPDATE_PRODUCT_BRAND)
    @Operation(summary = "Deactivate a product brand", description = "Marks the pair as inactive so it is no longer added to new surveys. Existing surveys keep it. Requires permission UPDATE_PRODUCT_BRAND.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product brand is inactive"),
        @ApiResponse(responseCode = "404", description = "Product brand not found")
    })
    public ResponseEntity<ProductBrandResponse> deactivate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Product brand ID", example = "e4d3c2b1-a0f9-4e8d-7c6b-5a4f3e2d1c0b") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateProductBrandStatusUseCase.execute(
            new UpdateProductBrandStatusUseCase.Input(requesterId, id, false)
        ));

        return ResponseEntity.ok(ProductBrandResponse.from(output));
    }
}

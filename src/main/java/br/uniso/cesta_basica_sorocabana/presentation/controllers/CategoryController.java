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

import br.uniso.cesta_basica_sorocabana.application.usecases.category.UpdateCategoryStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.category.CreateCategoryUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.category.FetchCategoryUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.category.FetchCategoriesUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.category.UpdateCategoryUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.enums.SortBy;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityUserDetails;
import br.uniso.cesta_basica_sorocabana.presentation.dto.category.CategoryResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.category.CreateCategoryRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.category.UpdateCategoryRequest;
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
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Product categories (e.g. Camil, Pilecco). Reading requires only a valid token; writing requires the permission listed on each route.")
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final FetchCategoryUseCase fetchCategoryUseCase;
    private final FetchCategoriesUseCase fetchCategoriesUseCase;
    private final UpdateCategoryStatusUseCase updateCategoryStatusUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping
    @Operation(summary = "List categories", description = "Returns a page of categories, active and inactive. Valid sortBy values: NAME_ASC, NAME_DESC.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of categories"),
        @ApiResponse(responseCode = "400", description = "Invalid page, size or sortBy")
    })
    public ResponseEntity<PageResponse<CategoryResponse>> list(
            @Parameter(description = "Page number, starting at 0", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Items per page, from 1 to 100", example = "20") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort order", example = "NAME_ASC") @RequestParam(defaultValue = "NAME_ASC") SortBy sortBy) {

        var output = transactionalProxy.executeReadOnly(() -> fetchCategoriesUseCase.execute(
            new FetchCategoriesUseCase.Input(new DomainPageRequest(page, size, sortBy))
        ));

        return ResponseEntity.ok(PageResponse.from(output, CategoryResponse::from));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category", description = "Returns a single category by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category found"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryResponse> get(
            @Parameter(description = "Category ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchCategoryUseCase.execute(new FetchCategoryUseCase.Input(id)));

        return ResponseEntity.ok(CategoryResponse.from(output));
    }

    @PostMapping
    @RequiresPermission(Permission.CREATE_CATEGORY)
    @Operation(summary = "Create a category", description = "Registers a new active category. Requires permission CREATE_CATEGORY.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Category created"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "409", description = "A category with this name already exists")
    })
    public ResponseEntity<CategoryResponse> create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateCategoryRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createCategoryUseCase.execute(new CreateCategoryUseCase.Input(requesterId, request.name())));

        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryResponse.from(output));
    }

    @PatchMapping("/{id}")
    @RequiresPermission(Permission.UPDATE_CATEGORY)
    @Operation(summary = "Rename a category", description = "Changes the name of an existing category. Requires permission UPDATE_CATEGORY.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category updated"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Another category already has this name")
    })
    public ResponseEntity<CategoryResponse> update(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Category ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id,
            @RequestBody @Valid UpdateCategoryRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateCategoryUseCase.execute(new UpdateCategoryUseCase.Input(requesterId, id, request.name())));

        return ResponseEntity.ok(CategoryResponse.from(output));
    }

    @PatchMapping("/{id}/activate")
    @RequiresPermission(Permission.UPDATE_CATEGORY)
    @Operation(summary = "Activate a category", description = "Marks the category as active again. Does nothing if it is already active. Requires permission UPDATE_CATEGORY.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category is active"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryResponse> activate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Category ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateCategoryStatusUseCase.execute(new UpdateCategoryStatusUseCase.Input(requesterId, id, true)));

        return ResponseEntity.ok(CategoryResponse.from(output));
    }

    @PatchMapping("/{id}/deactivate")
    @RequiresPermission(Permission.UPDATE_CATEGORY)
    @Operation(summary = "Deactivate a category", description = "Marks the category as inactive so it can no longer be used by new products. Existing records keep it. Requires permission UPDATE_CATEGORY.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category is inactive"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryResponse> deactivate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Category ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateCategoryStatusUseCase.execute(new UpdateCategoryStatusUseCase.Input(requesterId, id, false)));

        return ResponseEntity.ok(CategoryResponse.from(output));
    }
}

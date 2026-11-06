package br.uniso.cesta_basica_sorocabana.presentation.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.uniso.cesta_basica_sorocabana.application.usecases.basket.FetchStoreBasketUseCase;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.presentation.dto.basket.BasketItemResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Basket", description = "The basket: the active items that can be surveyed in each store. Requires only a valid token.")
public class BasketController {

    private final FetchStoreBasketUseCase fetchStoreBasketUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping("/stores/{storeId}/items")
    @Operation(summary = "List the active items of a store", description = "Returns every active item linked to the store, with everything about it. Not paginated: the screen needs the whole list. An inactive store has no items.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active items of the store"),
        @ApiResponse(responseCode = "404", description = "Store not found")
    })
    public ResponseEntity<List<BasketItemResponse>> list(
            @Parameter(description = "Store ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f") @PathVariable UUID storeId) {

        var output = transactionalProxy.executeReadOnly(() -> fetchStoreBasketUseCase.execute(new FetchStoreBasketUseCase.Input(storeId)));

        return ResponseEntity.ok(output.stream().map(BasketItemResponse::from).toList());
    }
}

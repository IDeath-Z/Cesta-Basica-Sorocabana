package br.uniso.cesta_basica_sorocabana.application.usecases.basket;

import java.util.List;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.StoreApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.BasketQuery;
import br.uniso.cesta_basica_sorocabana.domain.ports.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchStoreBasketUseCase {

    private final StoreRepository storeRepository;
    private final BasketQuery basketQuery;

    public record Input(UUID storeId) {}

    public List<BasketItemOutput> execute(Input input) {
        if (storeRepository.findById(input.storeId()).isEmpty())
            throw new ResourceNotFoundException(StoreApplicationError.NOT_FOUND.message(input.storeId()));

        return basketQuery.findActiveByStoreId(input.storeId()).stream()
            .map(BasketItemOutput::from)
            .toList();
    }
}

package br.uniso.cesta_basica_sorocabana.application.usecases.store;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.StoreApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchStoreUseCase {

    private final StoreRepository storeRepository;

    public record Input(UUID id) {}

    public StoreOutput execute(Input input) {
        return storeRepository.findById(input.id())
            .map(StoreOutput::from)
            .orElseThrow(() -> new ResourceNotFoundException(StoreApplicationError.NOT_FOUND.message(input.id())));
    }
}

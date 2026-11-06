package br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductBrandStoreApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandStoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchProductBrandStoreUseCase {

    private final ProductBrandStoreRepository productBrandStoreRepository;

    public record Input(UUID id) {}

    public ProductBrandStoreOutput execute(Input input) {
        return productBrandStoreRepository.findById(input.id())
            .map(ProductBrandStoreOutput::from)
            .orElseThrow(() -> new ResourceNotFoundException(ProductBrandStoreApplicationError.NOT_FOUND.message(input.id())));
    }
}

package br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore;

import java.util.List;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductBrandApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandStoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchProductBrandStoresUseCase {

    private final ProductBrandStoreRepository productBrandStoreRepository;
    private final ProductBrandRepository productBrandRepository;

    public record Input(UUID productBrandId) {}

    public List<ProductBrandStoreOutput> execute(Input input) {
        if (productBrandRepository.findById(input.productBrandId()).isEmpty())
            throw new ResourceNotFoundException(ProductBrandApplicationError.NOT_FOUND.message(input.productBrandId()));

        return productBrandStoreRepository.findByProductBrandId(input.productBrandId()).stream()
            .map(ProductBrandStoreOutput::from)
            .toList();
    }
}

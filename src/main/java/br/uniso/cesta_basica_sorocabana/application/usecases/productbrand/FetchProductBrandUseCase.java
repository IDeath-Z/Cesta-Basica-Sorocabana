package br.uniso.cesta_basica_sorocabana.application.usecases.productbrand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductBrandApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchProductBrandUseCase {

    private final ProductBrandRepository productBrandRepository;

    public record Input(UUID id) {}

    public ProductBrandOutput execute(Input input) {
        return productBrandRepository.findById(input.id())
            .map(ProductBrandOutput::from)
            .orElseThrow(() -> new ResourceNotFoundException(ProductBrandApplicationError.NOT_FOUND.message(input.id())));
    }
}

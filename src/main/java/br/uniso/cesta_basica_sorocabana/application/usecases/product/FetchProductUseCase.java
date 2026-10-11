package br.uniso.cesta_basica_sorocabana.application.usecases.product;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchProductUseCase {

    private final ProductRepository productRepository;

    public record Input(UUID id) {}

    public ProductOutput execute(Input input) {
        return productRepository.findById(input.id())
            .map(ProductOutput::from)
            .orElseThrow(() -> new ResourceNotFoundException(ProductApplicationError.NOT_FOUND.message(input.id())));
    }
}

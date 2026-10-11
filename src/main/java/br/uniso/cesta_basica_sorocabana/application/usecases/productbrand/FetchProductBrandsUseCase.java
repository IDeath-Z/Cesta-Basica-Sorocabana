package br.uniso.cesta_basica_sorocabana.application.usecases.productbrand;

import java.util.List;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchProductBrandsUseCase {

    private final ProductBrandRepository productBrandRepository;
    private final ProductRepository productRepository;

    public record Input(UUID productId) {}

    public List<ProductBrandOutput> execute(Input input) {
        if (productRepository.findById(input.productId()).isEmpty())
            throw new ResourceNotFoundException(ProductApplicationError.NOT_FOUND.message(input.productId()));

        return productBrandRepository.findByProductId(input.productId()).stream()
            .map(ProductBrandOutput::from)
            .toList();
    }
}

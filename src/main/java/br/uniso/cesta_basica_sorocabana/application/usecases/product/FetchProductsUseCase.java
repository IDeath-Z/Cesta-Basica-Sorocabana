package br.uniso.cesta_basica_sorocabana.application.usecases.product;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.ports.ProductRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchProductsUseCase {

    private final ProductRepository productRepository;

    // categoryId is optional: when null, every product is listed.
    public record Input(UUID categoryId, DomainPageRequest pageRequest) {}

    public DomainPageResult<ProductOutput> execute(Input input) {
        var page = input.categoryId() == null
            ? productRepository.findAll(input.pageRequest())
            : productRepository.findByCategoryId(input.categoryId(), input.pageRequest());

        return page.map(ProductOutput::from);
    }
}

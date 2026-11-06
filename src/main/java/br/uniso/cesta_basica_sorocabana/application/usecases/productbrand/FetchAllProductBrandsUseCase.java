package br.uniso.cesta_basica_sorocabana.application.usecases.productbrand;

import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchAllProductBrandsUseCase {

    private final ProductBrandRepository productBrandRepository;

    public record Input(int page, int size) {}

    public DomainPageResult<ProductBrandSummaryOutput> execute(Input input) {
        return productBrandRepository.findAllSummaries(input.page(), input.size()).map(ProductBrandSummaryOutput::from);
    }
}

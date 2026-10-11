package br.uniso.cesta_basica_sorocabana.application.usecases.brand;

import br.uniso.cesta_basica_sorocabana.domain.ports.BrandRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchBrandsUseCase {

    private final BrandRepository brandRepository;

    public record Input(DomainPageRequest pageRequest) {}

    public DomainPageResult<BrandOutput> execute(Input input) {
        return brandRepository.findAll(input.pageRequest()).map(BrandOutput::from);
    }
}

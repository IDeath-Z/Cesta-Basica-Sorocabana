package br.uniso.cesta_basica_sorocabana.application.usecases.category;

import br.uniso.cesta_basica_sorocabana.domain.ports.CategoryRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchCategoriesUseCase {

    private final CategoryRepository categoryRepository;

    public record Input(DomainPageRequest pageRequest) {}

    public DomainPageResult<CategoryOutput> execute(Input input) {
        return categoryRepository.findAll(input.pageRequest()).map(CategoryOutput::from);
    }
}

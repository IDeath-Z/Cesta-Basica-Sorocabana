package br.uniso.cesta_basica_sorocabana.application.usecases.category;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.CategoryApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public record Input(UUID id) {}

    public CategoryOutput execute(Input input) {
        return categoryRepository.findById(input.id())
            .map(CategoryOutput::from)
            .orElseThrow(() -> new ResourceNotFoundException(CategoryApplicationError.NOT_FOUND.message(input.id())));
    }
}

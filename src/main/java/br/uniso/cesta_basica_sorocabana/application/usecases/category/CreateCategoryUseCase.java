package br.uniso.cesta_basica_sorocabana.application.usecases.category;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.CategoryApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Category;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, String name) {}

    public CategoryOutput execute(Input input) {
        if (categoryRepository.existsByName(input.name()))
            throw new ResourceAlreadyExistsException(CategoryApplicationError.NAME_ALREADY_EXISTS.message(input.name()));

        CategoryOutput output = CategoryOutput.from(categoryRepository.save(Category.createNew(input.name())));

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.CATEGORY, output.id(), output);
        return output;
    }
}

package br.uniso.cesta_basica_sorocabana.application.usecases.category;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.CategoryApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Category;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateCategoryStatusUseCase {

    private final CategoryRepository categoryRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, boolean active) {}

    // Already in the requested state: nothing changes, so nothing is saved or audited.
    public CategoryOutput execute(Input input) {
        Category category = categoryRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(CategoryApplicationError.NOT_FOUND.message(input.id())));

        Category changed = input.active() ? category.activate() : category.deactivate();
        if (changed == category)
            return CategoryOutput.from(category);

        CategoryOutput before = CategoryOutput.from(category);
        CategoryOutput after = CategoryOutput.from(categoryRepository.save(changed));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.CATEGORY, after.id(), before, after);
        return after;
    }
}

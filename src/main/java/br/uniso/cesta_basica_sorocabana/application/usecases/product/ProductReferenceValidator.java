package br.uniso.cesta_basica_sorocabana.application.usecases.product;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.CategoryApplicationError;
import br.uniso.cesta_basica_sorocabana.application.enums.errors.UnitApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.BusinessRuleException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.entities.Category;
import br.uniso.cesta_basica_sorocabana.domain.entities.Unit;
import br.uniso.cesta_basica_sorocabana.domain.ports.CategoryRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.UnitRepository;

import lombok.RequiredArgsConstructor;

// Shared by create and update: a product can only point to an existing, active category and unit.
@RequiredArgsConstructor
public final class ProductReferenceValidator {

    private final CategoryRepository categoryRepository;
    private final UnitRepository unitRepository;

    public void requireActiveCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException(CategoryApplicationError.NOT_FOUND.message(categoryId)));

        if (!category.isActive())
            throw new BusinessRuleException(CategoryApplicationError.INACTIVE.message(category.getName()));
    }

    public void requireActiveUnit(UUID unitId) {
        Unit unit = unitRepository.findById(unitId)
            .orElseThrow(() -> new ResourceNotFoundException(UnitApplicationError.NOT_FOUND.message(unitId)));

        if (!unit.isActive())
            throw new BusinessRuleException(UnitApplicationError.INACTIVE.message(unitId));
    }
}

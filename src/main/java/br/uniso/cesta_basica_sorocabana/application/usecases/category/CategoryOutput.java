package br.uniso.cesta_basica_sorocabana.application.usecases.category;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Category;

public record CategoryOutput(UUID id, String name, boolean active) {

    public static CategoryOutput from(Category category) {
        return new CategoryOutput(category.getId(), category.getName(), category.isActive());
    }
}

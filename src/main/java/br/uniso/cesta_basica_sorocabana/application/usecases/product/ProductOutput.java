package br.uniso.cesta_basica_sorocabana.application.usecases.product;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Product;

public record ProductOutput(UUID id, UUID categoryId, UUID unitId, String name, String specification, String notes,
    boolean active) {

    public static ProductOutput from(Product product) {
        return new ProductOutput(product.getId(), product.getCategoryId(), product.getUnitId(), product.getName(),
            product.getSpecification(), product.getNotes(), product.isActive());
    }
}

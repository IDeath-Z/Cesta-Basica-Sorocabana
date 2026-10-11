package br.uniso.cesta_basica_sorocabana.application.usecases.brand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Brand;

public record BrandOutput(UUID id, String name, boolean active) {

    public static BrandOutput from(Brand brand) {
        return new BrandOutput(brand.getId(), brand.getName(), brand.isActive());
    }
}

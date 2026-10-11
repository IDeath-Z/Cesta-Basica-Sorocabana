package br.uniso.cesta_basica_sorocabana.application.usecases.brand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.BrandApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Brand;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.BrandRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateBrandUseCase {

    private final BrandRepository brandRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, String name) {}

    public BrandOutput execute(Input input) {
        if (brandRepository.existsByName(input.name()))
            throw new ResourceAlreadyExistsException(BrandApplicationError.NAME_ALREADY_EXISTS.message(input.name()));

        BrandOutput output = BrandOutput.from(brandRepository.save(Brand.createNew(input.name())));

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.BRAND, output.id(), output);
        return output;
    }
}

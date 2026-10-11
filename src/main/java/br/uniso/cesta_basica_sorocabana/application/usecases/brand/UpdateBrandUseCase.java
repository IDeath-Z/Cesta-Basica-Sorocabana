package br.uniso.cesta_basica_sorocabana.application.usecases.brand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.BrandApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Brand;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.BrandRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateBrandUseCase {

    private final BrandRepository brandRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, String name) {}

    public BrandOutput execute(Input input) {
        Brand brand = brandRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(BrandApplicationError.NOT_FOUND.message(input.id())));

        if (!brand.getName().equals(input.name()) && brandRepository.existsByName(input.name()))
            throw new ResourceAlreadyExistsException(BrandApplicationError.NAME_ALREADY_EXISTS.message(input.name()));

        BrandOutput before = BrandOutput.from(brand);
        BrandOutput after = BrandOutput.from(brandRepository.save(brand.rename(input.name())));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.BRAND, after.id(), before, after);
        return after;
    }
}

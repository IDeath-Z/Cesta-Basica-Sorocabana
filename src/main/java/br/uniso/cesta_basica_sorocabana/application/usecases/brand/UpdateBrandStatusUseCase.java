package br.uniso.cesta_basica_sorocabana.application.usecases.brand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.BrandApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Brand;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.BrandRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateBrandStatusUseCase {

    private final BrandRepository brandRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, boolean active) {}

    // Already in the requested state: nothing changes, so nothing is saved or audited.
    public BrandOutput execute(Input input) {
        Brand brand = brandRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(BrandApplicationError.NOT_FOUND.message(input.id())));

        Brand changed = input.active() ? brand.activate() : brand.deactivate();
        if (changed == brand)
            return BrandOutput.from(brand);

        BrandOutput before = BrandOutput.from(brand);
        BrandOutput after = BrandOutput.from(brandRepository.save(changed));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.BRAND, after.id(), before, after);
        return after;
    }
}

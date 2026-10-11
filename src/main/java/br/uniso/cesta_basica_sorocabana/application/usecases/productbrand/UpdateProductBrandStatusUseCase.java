package br.uniso.cesta_basica_sorocabana.application.usecases.productbrand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductBrandApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrand;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateProductBrandStatusUseCase {

    private final ProductBrandRepository productBrandRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, boolean active) {}

    // Already in the requested state: nothing changes, so nothing is saved or audited.
    public ProductBrandOutput execute(Input input) {
        ProductBrand productBrand = productBrandRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(ProductBrandApplicationError.NOT_FOUND.message(input.id())));

        ProductBrand changed = input.active() ? productBrand.activate() : productBrand.deactivate();
        if (changed == productBrand)
            return ProductBrandOutput.from(productBrand);

        ProductBrandOutput before = ProductBrandOutput.from(productBrand);
        ProductBrandOutput after = ProductBrandOutput.from(productBrandRepository.save(changed));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.PRODUCT_BRAND, after.id(), before, after);
        return after;
    }
}

package br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductBrandStoreApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrandStore;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandStoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateProductBrandStoreStatusUseCase {

    private final ProductBrandStoreRepository productBrandStoreRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, boolean active) {}

    // Already in the requested state: nothing changes, so nothing is saved or audited.
    public ProductBrandStoreOutput execute(Input input) {
        ProductBrandStore link = productBrandStoreRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(ProductBrandStoreApplicationError.NOT_FOUND.message(input.id())));

        ProductBrandStore changed = input.active() ? link.activate() : link.deactivate();
        if (changed == link)
            return ProductBrandStoreOutput.from(link);

        ProductBrandStoreOutput before = ProductBrandStoreOutput.from(link);
        ProductBrandStoreOutput after = ProductBrandStoreOutput.from(productBrandStoreRepository.save(changed));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.PRODUCT_BRAND_STORE, after.id(), before, after);
        return after;
    }
}

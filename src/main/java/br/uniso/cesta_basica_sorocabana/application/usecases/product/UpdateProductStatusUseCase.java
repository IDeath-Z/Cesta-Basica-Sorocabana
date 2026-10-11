package br.uniso.cesta_basica_sorocabana.application.usecases.product;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Product;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateProductStatusUseCase {

    private final ProductRepository productRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, boolean active) {}

    // Already in the requested state: nothing changes, so nothing is saved or audited.
    public ProductOutput execute(Input input) {
        Product product = productRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(ProductApplicationError.NOT_FOUND.message(input.id())));

        Product changed = input.active() ? product.activate() : product.deactivate();
        if (changed == product)
            return ProductOutput.from(product);

        ProductOutput before = ProductOutput.from(product);
        ProductOutput after = ProductOutput.from(productRepository.save(changed));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.PRODUCT, after.id(), before, after);
        return after;
    }
}

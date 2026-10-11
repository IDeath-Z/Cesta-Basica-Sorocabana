package br.uniso.cesta_basica_sorocabana.application.usecases.product;

import java.util.Objects;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Product;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateProductUseCase {

    private final ProductRepository productRepository;
    private final ProductReferenceValidator referenceValidator;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, UUID categoryId, UUID unitId, String name, String specification, String notes) {}

    public ProductOutput execute(Input input) {
        Product product = productRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(ProductApplicationError.NOT_FOUND.message(input.id())));

        if (input.categoryId() != null && !input.categoryId().equals(product.getCategoryId()))
            referenceValidator.requireActiveCategory(input.categoryId());
        if (input.unitId() != null && !input.unitId().equals(product.getUnitId()))
            referenceValidator.requireActiveUnit(input.unitId());

        Product updated = product.update(input.categoryId(), input.unitId(), input.name(), input.specification(), input.notes());

        boolean identityChanged = !updated.getName().equals(product.getName())
            || !Objects.equals(updated.getSpecification(), product.getSpecification());
        if (identityChanged && productRepository.existsByNameAndSpecification(updated.getName(), updated.getSpecification()))
            throw new ResourceAlreadyExistsException(ProductApplicationError.ALREADY_EXISTS.message(updated.getName()));

        ProductOutput before = ProductOutput.from(product);
        ProductOutput after = ProductOutput.from(productRepository.save(updated));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.PRODUCT, after.id(), before, after);
        return after;
    }
}

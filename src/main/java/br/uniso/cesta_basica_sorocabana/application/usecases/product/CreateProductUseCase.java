package br.uniso.cesta_basica_sorocabana.application.usecases.product;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Product;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final ProductReferenceValidator referenceValidator;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID categoryId, UUID unitId, String name, String specification, String notes) {}

    public ProductOutput execute(Input input) {
        referenceValidator.requireActiveCategory(input.categoryId());
        if (input.unitId() != null)
            referenceValidator.requireActiveUnit(input.unitId());

        if (productRepository.existsByNameAndSpecification(input.name(), input.specification()))
            throw new ResourceAlreadyExistsException(ProductApplicationError.ALREADY_EXISTS.message(input.name()));

        Product product = Product.createNew(input.categoryId(), input.unitId(), input.name(), input.specification(), input.notes());
        ProductOutput output = ProductOutput.from(productRepository.save(product));

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.PRODUCT, output.id(), output);
        return output;
    }
}

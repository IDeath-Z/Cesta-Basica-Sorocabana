package br.uniso.cesta_basica_sorocabana.application.usecases.productbrand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.BrandApplicationError;
import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductApplicationError;
import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductBrandApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.BusinessRuleException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Brand;
import br.uniso.cesta_basica_sorocabana.domain.entities.Product;
import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrand;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.BrandRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateProductBrandUseCase {

    private final ProductBrandRepository productBrandRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID productId, UUID brandId) {}

    public ProductBrandOutput execute(Input input) {
        Product product = productRepository.findById(input.productId())
            .orElseThrow(() -> new ResourceNotFoundException(ProductApplicationError.NOT_FOUND.message(input.productId())));
        if (!product.isActive())
            throw new BusinessRuleException(ProductApplicationError.INACTIVE.message(product.getName()));

        Brand brand = brandRepository.findById(input.brandId())
            .orElseThrow(() -> new ResourceNotFoundException(BrandApplicationError.NOT_FOUND.message(input.brandId())));
        if (!brand.isActive())
            throw new BusinessRuleException(BrandApplicationError.INACTIVE.message(brand.getName()));

        if (productBrandRepository.existsByProductIdAndBrandId(input.productId(), input.brandId()))
            throw new ResourceAlreadyExistsException(ProductBrandApplicationError.ALREADY_EXISTS.message());

        ProductBrand productBrand = ProductBrand.createNew(input.productId(), input.brandId());
        ProductBrandOutput output = ProductBrandOutput.from(productBrandRepository.save(productBrand));

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.PRODUCT_BRAND, output.id(), output);
        return output;
    }
}

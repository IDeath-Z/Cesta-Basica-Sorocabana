package br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductBrandApplicationError;
import br.uniso.cesta_basica_sorocabana.application.enums.errors.ProductBrandStoreApplicationError;
import br.uniso.cesta_basica_sorocabana.application.enums.errors.StoreApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.BusinessRuleException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrand;
import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrandStore;
import br.uniso.cesta_basica_sorocabana.domain.entities.Store;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandStoreRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateProductBrandStoreUseCase {

    private final ProductBrandStoreRepository productBrandStoreRepository;
    private final ProductBrandRepository productBrandRepository;
    private final StoreRepository storeRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID productBrandId, UUID storeId, String searchName) {}

    public ProductBrandStoreOutput execute(Input input) {
        ProductBrand productBrand = productBrandRepository.findById(input.productBrandId())
            .orElseThrow(() -> new ResourceNotFoundException(ProductBrandApplicationError.NOT_FOUND.message(input.productBrandId())));
        if (!productBrand.isActive())
            throw new BusinessRuleException(ProductBrandApplicationError.INACTIVE.message(productBrand.getId()));

        Store store = storeRepository.findById(input.storeId())
            .orElseThrow(() -> new ResourceNotFoundException(StoreApplicationError.NOT_FOUND.message(input.storeId())));
        if (!store.isActive())
            throw new BusinessRuleException(StoreApplicationError.INACTIVE.message(store.getName()));

        if (productBrandStoreRepository.existsByProductBrandIdAndStoreId(input.productBrandId(), input.storeId()))
            throw new ResourceAlreadyExistsException(ProductBrandStoreApplicationError.ALREADY_EXISTS.message());

        ProductBrandStore link = ProductBrandStore.createNew(input.productBrandId(), input.storeId(), input.searchName());
        ProductBrandStoreOutput output = ProductBrandStoreOutput.from(productBrandStoreRepository.save(link));

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.PRODUCT_BRAND_STORE, output.id(), output);
        return output;
    }
}

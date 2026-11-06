package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore.CreateProductBrandStoreUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore.FetchProductBrandStoreUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore.FetchProductBrandStoresUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore.UpdateProductBrandStoreStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore.UpdateProductBrandStoreUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandStoreRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.StoreRepository;

@Configuration
public class ProductBrandStoreConfig {

    @Bean
    public CreateProductBrandStoreUseCase createProductBrandStoreUseCase(ProductBrandStoreRepository productBrandStoreRepository, ProductBrandRepository productBrandRepository, StoreRepository storeRepository, AuditRecorder auditRecorder) {
        return new CreateProductBrandStoreUseCase(productBrandStoreRepository, productBrandRepository, storeRepository, auditRecorder);
    }

    @Bean
    public UpdateProductBrandStoreUseCase updateProductBrandStoreUseCase(ProductBrandStoreRepository productBrandStoreRepository, AuditRecorder auditRecorder) {
        return new UpdateProductBrandStoreUseCase(productBrandStoreRepository, auditRecorder);
    }

    @Bean
    public UpdateProductBrandStoreStatusUseCase updateProductBrandStoreStatusUseCase(ProductBrandStoreRepository productBrandStoreRepository, AuditRecorder auditRecorder) {
        return new UpdateProductBrandStoreStatusUseCase(productBrandStoreRepository, auditRecorder);
    }

    @Bean
    public FetchProductBrandStoreUseCase fetchProductBrandStoreUseCase(ProductBrandStoreRepository productBrandStoreRepository) {
        return new FetchProductBrandStoreUseCase(productBrandStoreRepository);
    }

    @Bean
    public FetchProductBrandStoresUseCase fetchProductBrandStoresUseCase(ProductBrandStoreRepository productBrandStoreRepository, ProductBrandRepository productBrandRepository) {
        return new FetchProductBrandStoresUseCase(productBrandStoreRepository, productBrandRepository);
    }
}

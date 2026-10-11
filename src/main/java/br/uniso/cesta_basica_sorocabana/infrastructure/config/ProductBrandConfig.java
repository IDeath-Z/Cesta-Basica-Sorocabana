package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrand.UpdateProductBrandStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrand.CreateProductBrandUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrand.FetchProductBrandUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.productbrand.FetchProductBrandsUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.BrandRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductRepository;

@Configuration
public class ProductBrandConfig {

    @Bean
    public CreateProductBrandUseCase createProductBrandUseCase(ProductBrandRepository productBrandRepository, ProductRepository productRepository, BrandRepository brandRepository, AuditRecorder auditRecorder) {
        return new CreateProductBrandUseCase(productBrandRepository, productRepository, brandRepository, auditRecorder);
    }

    @Bean
    public FetchProductBrandUseCase fetchProductBrandUseCase(ProductBrandRepository productBrandRepository) {
        return new FetchProductBrandUseCase(productBrandRepository);
    }

    @Bean
    public FetchProductBrandsUseCase fetchProductBrandsUseCase(ProductBrandRepository productBrandRepository, ProductRepository productRepository) {
        return new FetchProductBrandsUseCase(productBrandRepository, productRepository);
    }

    @Bean
    public UpdateProductBrandStatusUseCase updateProductBrandStatusUseCase(ProductBrandRepository productBrandRepository, AuditRecorder auditRecorder) {
        return new UpdateProductBrandStatusUseCase(productBrandRepository, auditRecorder);
    }
}

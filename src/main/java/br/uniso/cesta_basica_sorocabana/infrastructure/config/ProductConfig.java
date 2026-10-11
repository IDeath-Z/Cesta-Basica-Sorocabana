package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.product.UpdateProductStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.product.CreateProductUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.product.FetchProductUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.product.FetchProductsUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.product.ProductReferenceValidator;
import br.uniso.cesta_basica_sorocabana.application.usecases.product.UpdateProductUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.CategoryRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.UnitRepository;

@Configuration
public class ProductConfig {

    @Bean
    public ProductReferenceValidator productReferenceValidator(CategoryRepository categoryRepository, UnitRepository unitRepository) {
        return new ProductReferenceValidator(categoryRepository, unitRepository);
    }

    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepository productRepository, ProductReferenceValidator productReferenceValidator, AuditRecorder auditRecorder) {
        return new CreateProductUseCase(productRepository, productReferenceValidator, auditRecorder);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(ProductRepository productRepository, ProductReferenceValidator productReferenceValidator, AuditRecorder auditRecorder) {
        return new UpdateProductUseCase(productRepository, productReferenceValidator, auditRecorder);
    }

    @Bean
    public FetchProductUseCase fetchProductUseCase(ProductRepository productRepository) {
        return new FetchProductUseCase(productRepository);
    }

    @Bean
    public FetchProductsUseCase fetchProductsUseCase(ProductRepository productRepository) {
        return new FetchProductsUseCase(productRepository);
    }

    @Bean
    public UpdateProductStatusUseCase updateProductStatusUseCase(ProductRepository productRepository, AuditRecorder auditRecorder) {
        return new UpdateProductStatusUseCase(productRepository, auditRecorder);
    }
}

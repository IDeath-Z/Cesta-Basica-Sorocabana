package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.category.UpdateCategoryStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.category.CreateCategoryUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.category.FetchCategoriesUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.category.FetchCategoryUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.category.UpdateCategoryUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.CategoryRepository;

@Configuration
public class CategoryConfig {

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(CategoryRepository categoryRepository, AuditRecorder auditRecorder) {
        return new CreateCategoryUseCase(categoryRepository, auditRecorder);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(CategoryRepository categoryRepository, AuditRecorder auditRecorder) {
        return new UpdateCategoryUseCase(categoryRepository, auditRecorder);
    }

    @Bean
    public FetchCategoryUseCase fetchCategoryUseCase(CategoryRepository categoryRepository) {
        return new FetchCategoryUseCase(categoryRepository);
    }

    @Bean
    public FetchCategoriesUseCase fetchCategoriesUseCase(CategoryRepository categoryRepository) {
        return new FetchCategoriesUseCase(categoryRepository);
    }

    @Bean
    public UpdateCategoryStatusUseCase updateCategoryStatusUseCase(CategoryRepository categoryRepository, AuditRecorder auditRecorder) {
        return new UpdateCategoryStatusUseCase(categoryRepository, auditRecorder);
    }
}

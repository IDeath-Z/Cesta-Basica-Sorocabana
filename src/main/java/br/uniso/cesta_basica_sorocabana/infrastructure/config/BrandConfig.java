package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.brand.UpdateBrandStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.brand.CreateBrandUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.brand.FetchBrandUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.brand.FetchBrandsUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.brand.UpdateBrandUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.BrandRepository;

@Configuration
public class BrandConfig {

    @Bean
    public CreateBrandUseCase createBrandUseCase(BrandRepository brandRepository, AuditRecorder auditRecorder) {
        return new CreateBrandUseCase(brandRepository, auditRecorder);
    }

    @Bean
    public UpdateBrandUseCase updateBrandUseCase(BrandRepository brandRepository, AuditRecorder auditRecorder) {
        return new UpdateBrandUseCase(brandRepository, auditRecorder);
    }

    @Bean
    public FetchBrandUseCase fetchBrandUseCase(BrandRepository brandRepository) {
        return new FetchBrandUseCase(brandRepository);
    }

    @Bean
    public FetchBrandsUseCase fetchBrandsUseCase(BrandRepository brandRepository) {
        return new FetchBrandsUseCase(brandRepository);
    }

    @Bean
    public UpdateBrandStatusUseCase updateBrandStatusUseCase(BrandRepository brandRepository, AuditRecorder auditRecorder) {
        return new UpdateBrandStatusUseCase(brandRepository, auditRecorder);
    }
}

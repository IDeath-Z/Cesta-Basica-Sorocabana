package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.store.UpdateStoreStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.store.CreateStoreUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.store.FetchStoreUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.store.FetchStoresUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.store.UpdateStoreUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.StoreRepository;

@Configuration
public class StoreConfig {

    @Bean
    public CreateStoreUseCase createStoreUseCase(StoreRepository storeRepository, AuditRecorder auditRecorder) {
        return new CreateStoreUseCase(storeRepository, auditRecorder);
    }

    @Bean
    public UpdateStoreUseCase updateStoreUseCase(StoreRepository storeRepository, AuditRecorder auditRecorder) {
        return new UpdateStoreUseCase(storeRepository, auditRecorder);
    }

    @Bean
    public FetchStoreUseCase fetchStoreUseCase(StoreRepository storeRepository) {
        return new FetchStoreUseCase(storeRepository);
    }

    @Bean
    public FetchStoresUseCase fetchStoresUseCase(StoreRepository storeRepository) {
        return new FetchStoresUseCase(storeRepository);
    }

    @Bean
    public UpdateStoreStatusUseCase updateStoreStatusUseCase(StoreRepository storeRepository, AuditRecorder auditRecorder) {
        return new UpdateStoreStatusUseCase(storeRepository, auditRecorder);
    }
}

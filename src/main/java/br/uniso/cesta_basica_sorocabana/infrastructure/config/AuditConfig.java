package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.audit.FetchAuditUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.audit.FetchAuditsUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.AuditRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.DataSerializer;

@Configuration
public class AuditConfig {

    @Bean
    public AuditRecorder auditRecorder(AuditRepository auditRepository, DataSerializer dataSerializer) {
        return new AuditRecorder(auditRepository, dataSerializer);
    }

    @Bean
    public FetchAuditUseCase fetchAuditUseCase(AuditRepository auditRepository) {
        return new FetchAuditUseCase(auditRepository);
    }

    @Bean
    public FetchAuditsUseCase fetchAuditsUseCase(AuditRepository auditRepository) {
        return new FetchAuditsUseCase(auditRepository);
    }
}

package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.unit.UpdateUnitStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.unit.CreateUnitUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.unit.FetchUnitUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.unit.FetchUnitsUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.unit.UpdateUnitUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.UnitRepository;

@Configuration
public class UnitConfig {

    @Bean
    public CreateUnitUseCase createUnitUseCase(UnitRepository unitRepository, AuditRecorder auditRecorder) {
        return new CreateUnitUseCase(unitRepository, auditRecorder);
    }

    @Bean
    public UpdateUnitUseCase updateUnitUseCase(UnitRepository unitRepository, AuditRecorder auditRecorder) {
        return new UpdateUnitUseCase(unitRepository, auditRecorder);
    }

    @Bean
    public FetchUnitUseCase fetchUnitUseCase(UnitRepository unitRepository) {
        return new FetchUnitUseCase(unitRepository);
    }

    @Bean
    public FetchUnitsUseCase fetchUnitsUseCase(UnitRepository unitRepository) {
        return new FetchUnitsUseCase(unitRepository);
    }

    @Bean
    public UpdateUnitStatusUseCase updateUnitStatusUseCase(UnitRepository unitRepository, AuditRecorder auditRecorder) {
        return new UpdateUnitStatusUseCase(unitRepository, auditRecorder);
    }
}

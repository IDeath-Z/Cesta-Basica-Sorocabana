package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.surveytype.UpdateSurveyTypeStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.surveytype.CreateSurveyTypeUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.surveytype.FetchSurveyTypeUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.surveytype.FetchSurveyTypesUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.surveytype.UpdateSurveyTypeUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyTypeRepository;

@Configuration
public class SurveyTypeConfig {

    @Bean
    public CreateSurveyTypeUseCase createSurveyTypeUseCase(SurveyTypeRepository surveyTypeRepository, AuditRecorder auditRecorder) {
        return new CreateSurveyTypeUseCase(surveyTypeRepository, auditRecorder);
    }

    @Bean
    public UpdateSurveyTypeUseCase updateSurveyTypeUseCase(SurveyTypeRepository surveyTypeRepository, AuditRecorder auditRecorder) {
        return new UpdateSurveyTypeUseCase(surveyTypeRepository, auditRecorder);
    }

    @Bean
    public FetchSurveyTypeUseCase fetchSurveyTypeUseCase(SurveyTypeRepository surveyTypeRepository) {
        return new FetchSurveyTypeUseCase(surveyTypeRepository);
    }

    @Bean
    public FetchSurveyTypesUseCase fetchSurveyTypesUseCase(SurveyTypeRepository surveyTypeRepository) {
        return new FetchSurveyTypesUseCase(surveyTypeRepository);
    }

    @Bean
    public UpdateSurveyTypeStatusUseCase updateSurveyTypeStatusUseCase(SurveyTypeRepository surveyTypeRepository, AuditRecorder auditRecorder) {
        return new UpdateSurveyTypeStatusUseCase(surveyTypeRepository, auditRecorder);
    }
}

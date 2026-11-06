package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.survey.CreateSurveyUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.survey.DeleteSurveyUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.survey.FetchSurveyUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.survey.FetchSurveysUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.survey.SaveSurveyPricesUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.survey.UpdateSurveyUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.BasketQuery;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyItemPriceRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyItemRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyTypeRepository;

@Configuration
public class SurveyConfig {

    @Bean
    public CreateSurveyUseCase createSurveyUseCase(SurveyRepository surveyRepository, SurveyTypeRepository surveyTypeRepository, AuditRecorder auditRecorder) {
        return new CreateSurveyUseCase(surveyRepository, surveyTypeRepository, auditRecorder);
    }

    @Bean
    public FetchSurveysUseCase fetchSurveysUseCase(SurveyRepository surveyRepository) {
        return new FetchSurveysUseCase(surveyRepository);
    }

    @Bean
    public FetchSurveyUseCase fetchSurveyUseCase(SurveyRepository surveyRepository, BasketQuery basketQuery) {
        return new FetchSurveyUseCase(surveyRepository, basketQuery);
    }

    @Bean
    public UpdateSurveyUseCase updateSurveyUseCase(SurveyRepository surveyRepository, SurveyTypeRepository surveyTypeRepository, AuditRecorder auditRecorder) {
        return new UpdateSurveyUseCase(surveyRepository, surveyTypeRepository, auditRecorder);
    }

    @Bean
    public DeleteSurveyUseCase deleteSurveyUseCase(SurveyRepository surveyRepository, SurveyItemRepository surveyItemRepository, SurveyItemPriceRepository surveyItemPriceRepository, BasketQuery basketQuery, AuditRecorder auditRecorder) {
        return new DeleteSurveyUseCase(surveyRepository, surveyItemRepository, surveyItemPriceRepository, basketQuery, auditRecorder);
    }

    @Bean
    public SaveSurveyPricesUseCase saveSurveyPricesUseCase(SurveyRepository surveyRepository, SurveyItemRepository surveyItemRepository, SurveyItemPriceRepository surveyItemPriceRepository, BasketQuery basketQuery, AuditRecorder auditRecorder) {
        return new SaveSurveyPricesUseCase(surveyRepository, surveyItemRepository, surveyItemPriceRepository, basketQuery, auditRecorder);
    }
}

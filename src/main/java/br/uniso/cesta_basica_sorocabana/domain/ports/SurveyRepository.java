package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Survey;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

public interface SurveyRepository {

    Survey save(Survey survey);
    Optional<Survey> findById(UUID id);
    DomainPageResult<Survey> findAll(DomainPageRequest pageRequest);
    DomainPageResult<Survey> findBySurveyTypeId(UUID surveyTypeId, DomainPageRequest pageRequest);
    boolean existsBySurveyDateAndSurveyTypeId(LocalDate surveyDate, UUID surveyTypeId);
    void deleteById(UUID id);
}

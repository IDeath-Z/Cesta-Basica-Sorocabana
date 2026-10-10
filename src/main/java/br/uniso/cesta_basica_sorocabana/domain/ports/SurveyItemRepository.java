package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyItem;

public interface SurveyItemRepository {

    SurveyItem save(SurveyItem surveyItem);
    Optional<SurveyItem> findById(UUID id);
    List<SurveyItem> findBySurveyId(UUID surveyId);
    boolean existsBySurveyIdAndProductBrandId(UUID surveyId, UUID productBrandId);
    void deleteById(UUID id);
}

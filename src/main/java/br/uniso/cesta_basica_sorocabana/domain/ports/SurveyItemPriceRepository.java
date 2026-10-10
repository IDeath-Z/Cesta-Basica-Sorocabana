package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyItemPrice;

public interface SurveyItemPriceRepository {

    SurveyItemPrice save(SurveyItemPrice surveyItemPrice);
    Optional<SurveyItemPrice> findById(UUID surveyItemId, UUID storeId);
    List<SurveyItemPrice> findBySurveyItemId(UUID surveyItemId);
    boolean existsById(UUID surveyItemId, UUID storeId);
    void deleteById(UUID surveyItemId, UUID storeId);
}

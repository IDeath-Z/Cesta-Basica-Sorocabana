package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.List;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.shared.BasketItem;

public interface BasketQuery {

    // Only items whose link, product + brand, product, brand and store are all active.
    List<BasketItem> findActiveByStoreId(UUID storeId);

    // What was saved in the survey, even if the item was deactivated afterwards.
    List<BasketItem> findSavedBySurveyId(UUID surveyId);
}

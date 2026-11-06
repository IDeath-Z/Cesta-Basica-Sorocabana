package br.uniso.cesta_basica_sorocabana.application.usecases.survey;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.basket.BasketItemOutput;
import br.uniso.cesta_basica_sorocabana.domain.entities.Survey;
import br.uniso.cesta_basica_sorocabana.domain.shared.BasketItem;

public record SurveyDetailOutput(UUID id, LocalDate surveyDate, UUID surveyTypeId, String notes, boolean draft, Instant createdAt,
    List<StoreOutput> stores) {

    public record StoreOutput(UUID storeId, String storeName, String storeUrl, List<BasketItemOutput> items) {}

    public static SurveyDetailOutput from(Survey survey, List<BasketItem> savedItems) {
        Map<UUID, StoreOutput> stores = new LinkedHashMap<>();
        for (BasketItem item : savedItems) {
            stores.computeIfAbsent(item.storeId(), id -> new StoreOutput(id, item.storeName(), item.storeUrl(), new ArrayList<>()))
                .items().add(BasketItemOutput.from(item));
        }

        return new SurveyDetailOutput(survey.getId(), survey.getSurveyDate(), survey.getSurveyTypeId(), survey.getNotes(),
            survey.isDraft(), survey.getCreatedAt(), List.copyOf(stores.values()));
    }
}

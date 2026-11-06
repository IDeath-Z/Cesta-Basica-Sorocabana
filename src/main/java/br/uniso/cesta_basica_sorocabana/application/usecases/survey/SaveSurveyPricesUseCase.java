package br.uniso.cesta_basica_sorocabana.application.usecases.survey;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.SurveyApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.BusinessRuleException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Survey;
import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyItem;
import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyItemPrice;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.BasketQuery;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyItemPriceRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyItemRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.BasketItem;

import lombok.RequiredArgsConstructor;

// Saves the prices of a draft survey, one item or the whole screen, in one transaction: an invalid line saves nothing.
// Items not sent stay as they are; a null price clears the item. A survey item exists only while it has a price.
// Prices have a composite key, so they are audited under their parent survey item, with the store inside the JSON.
@RequiredArgsConstructor
public final class SaveSurveyPricesUseCase {

    private final SurveyRepository surveyRepository;
    private final SurveyItemRepository surveyItemRepository;
    private final SurveyItemPriceRepository surveyItemPriceRepository;
    private final BasketQuery basketQuery;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID surveyId, List<StoreInput> stores) {}
    public record StoreInput(UUID storeId, List<ItemInput> items) {}
    public record ItemInput(UUID productBrandId, BigDecimal price) {}

    private record PriceKey(UUID surveyItemId, UUID storeId) {}

    public SurveyDetailOutput execute(Input input) {
        Survey survey = surveyRepository.findById(input.surveyId())
            .orElseThrow(() -> new ResourceNotFoundException(SurveyApplicationError.NOT_FOUND.message(input.surveyId())));
        if (!survey.isDraft())
            throw new BusinessRuleException(SurveyApplicationError.NOT_DRAFT.message(survey.getId()));

        rejectDuplicates(input.stores());

        Map<UUID, SurveyItem> surveyItems = surveyItemRepository.findBySurveyId(survey.getId()).stream()
            .collect(Collectors.toMap(SurveyItem::getProductBrandId, item -> item));
        Map<PriceKey, SurveyItemPrice> prices = surveyItemPriceRepository.findBySurveyId(survey.getId()).stream()
            .collect(Collectors.toMap(price -> new PriceKey(price.getSurveyItemId(), price.getStoreId()), price -> price, (a, b) -> a, HashMap::new));
        Set<UUID> clearedSurveyItems = new HashSet<>();

        for (StoreInput store : input.stores()) {
            Set<UUID> activeItems = basketQuery.findActiveByStoreId(store.storeId()).stream()
                .map(BasketItem::productBrandId)
                .collect(Collectors.toSet());

            for (ItemInput item : store.items()) {
                if (item.price() == null) {
                    clear(input.requesterId(), surveyItems.get(item.productBrandId()), store.storeId(), prices, clearedSurveyItems);
                    continue;
                }

                if (!activeItems.contains(item.productBrandId()))
                    throw new BusinessRuleException(SurveyApplicationError.ITEM_NOT_ACTIVE.message(item.productBrandId(), store.storeId()));

                SurveyItem surveyItem = surveyItems.computeIfAbsent(item.productBrandId(),
                    productBrandId -> surveyItemRepository.save(SurveyItem.createNew(survey.getId(), productBrandId)));
                save(input.requesterId(), surveyItem, store.storeId(), item.price(), prices);
            }
        }

        clearedSurveyItems.stream()
            .filter(surveyItemId -> prices.keySet().stream().noneMatch(key -> key.surveyItemId().equals(surveyItemId)))
            .forEach(surveyItemRepository::deleteById);

        return SurveyDetailOutput.from(survey, basketQuery.findSavedBySurveyId(survey.getId()));
    }

    private void rejectDuplicates(List<StoreInput> stores) {
        Set<String> seen = new HashSet<>();
        for (StoreInput store : stores) {
            for (ItemInput item : store.items()) {
                if (!seen.add(store.storeId() + "/" + item.productBrandId()))
                    throw new BusinessRuleException(SurveyApplicationError.DUPLICATE_ITEM.message(item.productBrandId(), store.storeId()));
            }
        }
    }

    // Same value: nothing changes, so nothing is saved or audited.
    private void save(UUID requesterId, SurveyItem surveyItem, UUID storeId, BigDecimal value, Map<PriceKey, SurveyItemPrice> prices) {
        PriceKey key = new PriceKey(surveyItem.getId(), storeId);
        SurveyItemPrice current = prices.get(key);

        if (current == null) {
            SurveyItemPrice created = surveyItemPriceRepository.save(SurveyItemPrice.createNew(surveyItem.getId(), storeId, value));
            prices.put(key, created);
            auditRecorder.recordInsert(requesterId, AuditedEntity.SURVEY_ITEM, surveyItem.getId(), SurveyItemPriceOutput.from(created));
            return;
        }

        if (current.getPrice().compareTo(value) == 0)
            return;

        SurveyItemPrice updated = surveyItemPriceRepository.save(current.updatePrice(value));
        prices.put(key, updated);
        auditRecorder.recordUpdate(requesterId, AuditedEntity.SURVEY_ITEM, surveyItem.getId(),
            SurveyItemPriceOutput.from(current), SurveyItemPriceOutput.from(updated));
    }

    // Nothing saved for this item in this store: nothing to clear.
    private void clear(UUID requesterId, SurveyItem surveyItem, UUID storeId, Map<PriceKey, SurveyItemPrice> prices, Set<UUID> clearedSurveyItems) {
        if (surveyItem == null)
            return;

        SurveyItemPrice current = prices.remove(new PriceKey(surveyItem.getId(), storeId));
        if (current == null)
            return;

        surveyItemPriceRepository.deleteById(surveyItem.getId(), storeId);
        auditRecorder.recordDelete(requesterId, AuditedEntity.SURVEY_ITEM, surveyItem.getId(), SurveyItemPriceOutput.from(current));
        clearedSurveyItems.add(surveyItem.getId());
    }
}

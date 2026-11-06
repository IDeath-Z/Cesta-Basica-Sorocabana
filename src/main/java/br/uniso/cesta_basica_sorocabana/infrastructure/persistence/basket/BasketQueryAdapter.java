package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.basket;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.ports.BasketQuery;
import br.uniso.cesta_basica_sorocabana.domain.shared.BasketItem;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class BasketQueryAdapter implements BasketQuery {

    private final BasketJpaRepository basketJpaRepository;

    @Override
    public List<BasketItem> findActiveByStoreId(UUID storeId) {
        return basketJpaRepository.findActiveByStoreId(storeId);
    }

    @Override
    public List<BasketItem> findSavedBySurveyId(UUID surveyId) {
        return basketJpaRepository.findSavedBySurveyId(surveyId);
    }
}

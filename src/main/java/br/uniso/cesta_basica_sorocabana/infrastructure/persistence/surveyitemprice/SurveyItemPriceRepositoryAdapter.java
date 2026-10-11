package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveyitemprice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyItemPrice;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyItemPriceRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class SurveyItemPriceRepositoryAdapter implements SurveyItemPriceRepository {

    private final SurveyItemPriceJpaRepository surveyItemPriceJpaRepository;
    private final SurveyItemPriceMapper surveyItemPriceMapper;

    @Override
    public SurveyItemPrice save(SurveyItemPrice surveyItemPrice) {
        SurveyItemPriceJpaEntity entity = surveyItemPriceMapper.toJpa(surveyItemPrice);
        return surveyItemPriceMapper.toDomain(surveyItemPriceJpaRepository.save(entity));
    }

    @Override
    public Optional<SurveyItemPrice> findById(UUID surveyItemId, UUID storeId) {
        return surveyItemPriceJpaRepository.findById(new SurveyItemPriceJpaId(surveyItemId, storeId))
            .map(surveyItemPriceMapper::toDomain);
    }

    @Override
    public List<SurveyItemPrice> findBySurveyItemId(UUID surveyItemId) {
        return surveyItemPriceJpaRepository.findBySurveyItemId(surveyItemId)
            .stream()
            .map(surveyItemPriceMapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsById(UUID surveyItemId, UUID storeId) {
        return surveyItemPriceJpaRepository.existsById(new SurveyItemPriceJpaId(surveyItemId, storeId));
    }

    @Override
    public void deleteById(UUID surveyItemId, UUID storeId) {
        surveyItemPriceJpaRepository.deleteById(new SurveyItemPriceJpaId(surveyItemId, storeId));
    }
}

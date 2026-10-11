package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveyitem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyItem;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyItemRepository;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand.ProductBrandJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand.ProductBrandJpaRepository;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.survey.SurveyJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.survey.SurveyJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class SurveyItemRepositoryAdapter implements SurveyItemRepository {

    private final SurveyItemJpaRepository surveyItemJpaRepository;
    private final SurveyJpaRepository surveyJpaRepository;
    private final ProductBrandJpaRepository productBrandJpaRepository;
    private final SurveyItemMapper surveyItemMapper;

    @Override
    public SurveyItem save(SurveyItem surveyItem) {
        SurveyJpaEntity surveyProxy = surveyJpaRepository.getReferenceById(surveyItem.getSurveyId());
        ProductBrandJpaEntity productBrandProxy = productBrandJpaRepository.getReferenceById(surveyItem.getProductBrandId());

        SurveyItemJpaEntity entity = surveyItemMapper.toJpa(surveyItem, surveyProxy, productBrandProxy);
        return surveyItemMapper.toDomain(surveyItemJpaRepository.save(entity));
    }

    @Override
    public Optional<SurveyItem> findById(UUID id) {
        return surveyItemJpaRepository.findById(id).map(surveyItemMapper::toDomain);
    }

    @Override
    public List<SurveyItem> findBySurveyId(UUID surveyId) {
        return surveyItemJpaRepository.findBySurveyId(surveyId)
            .stream()
            .map(surveyItemMapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsBySurveyIdAndProductBrandId(UUID surveyId, UUID productBrandId) {
        return surveyItemJpaRepository.existsBySurveyIdAndProductBrandId(surveyId, productBrandId);
    }

    @Override
    public void deleteById(UUID id) {
        surveyItemJpaRepository.deleteById(id);
    }
}

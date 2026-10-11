package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveyitem;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyItemJpaRepository extends JpaRepository<SurveyItemJpaEntity, UUID> {

    List<SurveyItemJpaEntity> findBySurveyId(UUID surveyId);
    boolean existsBySurveyIdAndProductBrandId(UUID surveyId, UUID productBrandId);
}

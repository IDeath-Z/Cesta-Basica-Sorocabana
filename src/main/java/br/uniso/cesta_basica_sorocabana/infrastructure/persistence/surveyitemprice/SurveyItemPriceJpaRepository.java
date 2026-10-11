package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveyitemprice;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SurveyItemPriceJpaRepository extends JpaRepository<SurveyItemPriceJpaEntity, SurveyItemPriceJpaId> {

    @Query("SELECT sip FROM SurveyItemPriceJpaEntity sip WHERE sip.id.surveyItemId = :surveyItemId")
    List<SurveyItemPriceJpaEntity> findBySurveyItemId(@Param("surveyItemId") UUID surveyItemId);
}

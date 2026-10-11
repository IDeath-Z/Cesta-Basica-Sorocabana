package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.survey;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyJpaRepository extends JpaRepository<SurveyJpaEntity, UUID> {

    Page<SurveyJpaEntity> findBySurveyTypeId(UUID surveyTypeId, Pageable pageable);
    boolean existsBySurveyDateAndSurveyTypeId(LocalDate surveyDate, UUID surveyTypeId);
}

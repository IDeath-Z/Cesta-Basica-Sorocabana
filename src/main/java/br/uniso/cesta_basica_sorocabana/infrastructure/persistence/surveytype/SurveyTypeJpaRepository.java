package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveytype;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyTypeJpaRepository extends JpaRepository<SurveyTypeJpaEntity, UUID> {

    Optional<SurveyTypeJpaEntity> findByCode(String code);
    boolean existsByCode(String code);
}

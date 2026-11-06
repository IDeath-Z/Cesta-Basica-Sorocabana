package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveytype;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyTypeJpaRepository extends JpaRepository<SurveyTypeJpaEntity, UUID> {

    boolean existsByName(String name);
}

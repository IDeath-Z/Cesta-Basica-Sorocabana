package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyType;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

public interface SurveyTypeRepository {

    SurveyType save(SurveyType surveyType);
    Optional<SurveyType> findById(UUID id);
    DomainPageResult<SurveyType> findAll(DomainPageRequest pageRequest);
    Optional<SurveyType> findByCode(String code);
    boolean existsByCode(String code);
}

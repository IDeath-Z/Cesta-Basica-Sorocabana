package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.survey;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.Survey;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.PageConverter;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveytype.SurveyTypeJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveytype.SurveyTypeJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class SurveyRepositoryAdapter implements SurveyRepository {

    private final SurveyJpaRepository surveyJpaRepository;
    private final SurveyTypeJpaRepository surveyTypeJpaRepository;
    private final SurveyMapper surveyMapper;

    @Override
    public Survey save(Survey survey) {
        SurveyTypeJpaEntity surveyTypeProxy = surveyTypeJpaRepository.getReferenceById(survey.getSurveyTypeId());

        SurveyJpaEntity entity = surveyMapper.toJpa(survey, surveyTypeProxy);
        return surveyMapper.toDomain(surveyJpaRepository.save(entity));
    }

    @Override
    public Optional<Survey> findById(UUID id) {
        return surveyJpaRepository.findById(id).map(surveyMapper::toDomain);
    }

    @Override
    public DomainPageResult<Survey> findAll(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<SurveyJpaEntity> page = surveyJpaRepository.findAll(springPage);

        return PageConverter.toDomain(page, surveyMapper::toDomain);
    }

    @Override
    public DomainPageResult<Survey> findBySurveyTypeId(UUID surveyTypeId, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<SurveyJpaEntity> page = surveyJpaRepository.findBySurveyTypeId(surveyTypeId, springPage);

        return PageConverter.toDomain(page, surveyMapper::toDomain);
    }

    @Override
    public boolean existsBySurveyDateAndSurveyTypeId(LocalDate surveyDate, UUID surveyTypeId) {
        return surveyJpaRepository.existsBySurveyDateAndSurveyTypeId(surveyDate, surveyTypeId);
    }

    // No ON DELETE CASCADE: fails if the survey still has survey items.
    @Override
    public void deleteById(UUID id) {
        surveyJpaRepository.deleteById(id);
    }
}

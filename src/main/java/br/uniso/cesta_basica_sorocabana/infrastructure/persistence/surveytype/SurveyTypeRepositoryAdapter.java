package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveytype;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyType;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyTypeRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.PageConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class SurveyTypeRepositoryAdapter implements SurveyTypeRepository {

    private final SurveyTypeJpaRepository surveyTypeJpaRepository;
    private final SurveyTypeMapper surveyTypeMapper;

    @Override
    public SurveyType save(SurveyType surveyType) {
        SurveyTypeJpaEntity entity = surveyTypeMapper.toJpa(surveyType);
        return surveyTypeMapper.toDomain(surveyTypeJpaRepository.save(entity));
    }

    @Override
    public Optional<SurveyType> findById(UUID id) {
        return surveyTypeJpaRepository.findById(id).map(surveyTypeMapper::toDomain);
    }

    @Override
    public DomainPageResult<SurveyType> findAll(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<SurveyTypeJpaEntity> page = surveyTypeJpaRepository.findAll(springPage);

        return PageConverter.toDomain(page, surveyTypeMapper::toDomain);
    }

    @Override
    public Optional<SurveyType> findByCode(String code) {
        return surveyTypeJpaRepository.findByCode(code).map(surveyTypeMapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return surveyTypeJpaRepository.existsByCode(code);
    }
}

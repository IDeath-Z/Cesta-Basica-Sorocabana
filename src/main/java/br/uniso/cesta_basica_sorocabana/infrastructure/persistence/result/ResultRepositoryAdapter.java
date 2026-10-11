package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.result;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.Result;
import br.uniso.cesta_basica_sorocabana.domain.ports.ResultRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class ResultRepositoryAdapter implements ResultRepository {

    private final ResultJpaRepository resultJpaRepository;
    private final ResultMapper resultMapper;

    @Override
    public Result save(Result result) {
        ResultJpaEntity entity = resultMapper.toJpa(result);
        return resultMapper.toDomain(resultJpaRepository.save(entity));
    }

    @Override
    public Optional<Result> findBySurveyItemId(UUID surveyItemId) {
        return resultJpaRepository.findById(surveyItemId).map(resultMapper::toDomain);
    }

    @Override
    public void deleteBySurveyItemId(UUID surveyItemId) {
        resultJpaRepository.deleteById(surveyItemId);
    }
}

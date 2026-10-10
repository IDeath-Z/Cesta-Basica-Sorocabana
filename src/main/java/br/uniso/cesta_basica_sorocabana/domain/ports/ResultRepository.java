package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Result;

public interface ResultRepository {

    Result save(Result result);
    Optional<Result> findBySurveyItemId(UUID surveyItemId);
    void deleteBySurveyItemId(UUID surveyItemId);
}

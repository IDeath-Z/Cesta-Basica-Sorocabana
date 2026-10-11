package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.result;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.Result;

@Component
public final class ResultMapper {

    public ResultJpaEntity toJpa(Result result) {
        ResultJpaEntity entity = new ResultJpaEntity();
        entity.setSurveyItemId(result.getSurveyItemId());
        entity.setAveragePrice(result.getAveragePrice());
        entity.setLowestPrice(result.getLowestPrice());
        entity.setHighestPrice(result.getHighestPrice());
        entity.setDifferenceAmount(result.getDifferenceAmount());
        entity.setDifferencePercent(result.getDifferencePercent());
        entity.setLowestPriceStoreIds(new HashSet<>(result.getLowestPriceStoreIds()));
        return entity;
    }

    public Result toDomain(ResultJpaEntity entity) {
        return Result.restore(
            entity.getSurveyItemId(),
            entity.getAveragePrice(),
            entity.getLowestPrice(),
            entity.getHighestPrice(),
            entity.getDifferenceAmount(),
            entity.getDifferencePercent(),
            entity.getLowestPriceStoreIds()
        );
    }
}

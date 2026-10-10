package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.ResultError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

// differencePercent is a fraction, not a percentage: 0.4016 = 40,16%.
// lowestPriceStoreIds holds more than one store when there is a tie.
public final class Result {

    private final UUID surveyItemId;
    private final BigDecimal averagePrice;
    private final BigDecimal lowestPrice;
    private final BigDecimal highestPrice;
    private final BigDecimal differenceAmount;
    private final BigDecimal differencePercent;
    private final Set<UUID> lowestPriceStoreIds;

    private Result(UUID surveyItemId, BigDecimal averagePrice, BigDecimal lowestPrice, BigDecimal highestPrice,
        BigDecimal differenceAmount, BigDecimal differencePercent, Set<UUID> lowestPriceStoreIds) {
        this.surveyItemId = DomainValidator.requireNonNull(surveyItemId, ResultError.SURVEY_ITEM_ID_REQUIRED.message());
        this.averagePrice = DomainValidator.requireNonNull(averagePrice, ResultError.AVERAGE_PRICE_REQUIRED.message());
        this.lowestPrice = DomainValidator.requireNonNull(lowestPrice, ResultError.LOWEST_PRICE_REQUIRED.message());
        this.highestPrice = DomainValidator.requireNonNull(highestPrice, ResultError.HIGHEST_PRICE_REQUIRED.message());
        this.differenceAmount = DomainValidator.requireNonNegative(differenceAmount, ResultError.DIFFERENCE_AMOUNT_NEGATIVE.message());
        this.differencePercent = DomainValidator.requireNonNegative(differencePercent, ResultError.DIFFERENCE_PERCENT_NEGATIVE.message());
        this.lowestPriceStoreIds = lowestPriceStoreIds == null ? Set.of() : Set.copyOf(lowestPriceStoreIds);

        if (lowestPrice.compareTo(highestPrice) > 0) {
            throw new IllegalArgumentException(ResultError.LOWEST_PRICE_GREATER_THAN_HIGHEST.message());
        }
    }

    public static Result createNew(UUID surveyItemId, BigDecimal averagePrice, BigDecimal lowestPrice,
        BigDecimal highestPrice, BigDecimal differenceAmount, BigDecimal differencePercent, Set<UUID> lowestPriceStoreIds) {
        return new Result(surveyItemId, averagePrice, lowestPrice, highestPrice, differenceAmount, differencePercent,
            lowestPriceStoreIds);
    }

    public static Result restore(UUID surveyItemId, BigDecimal averagePrice, BigDecimal lowestPrice,
        BigDecimal highestPrice, BigDecimal differenceAmount, BigDecimal differencePercent, Set<UUID> lowestPriceStoreIds) {
        return new Result(surveyItemId, averagePrice, lowestPrice, highestPrice, differenceAmount, differencePercent,
            lowestPriceStoreIds);
    }

    public UUID getSurveyItemId() {
        return surveyItemId;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public BigDecimal getHighestPrice() {
        return highestPrice;
    }

    public BigDecimal getDifferenceAmount() {
        return differenceAmount;
    }

    public BigDecimal getDifferencePercent() {
        return differencePercent;
    }

    public Set<UUID> getLowestPriceStoreIds() {
        return lowestPriceStoreIds;
    }
}

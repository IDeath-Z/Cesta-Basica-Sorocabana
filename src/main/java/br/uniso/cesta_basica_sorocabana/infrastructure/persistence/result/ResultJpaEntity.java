package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.result;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "result")
public class ResultJpaEntity {

    // Shares the primary key with survey_item (1:1).
    @Id
    @Column(name = "survey_item_id", nullable = false, updatable = false)
    private UUID surveyItemId;

    @Column(name = "average_price", nullable = false, precision = 12, scale = 4)
    private BigDecimal averagePrice;

    @Column(name = "lowest_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal lowestPrice;

    @Column(name = "highest_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal highestPrice;

    @Column(name = "difference_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal differenceAmount;

    @Column(name = "difference_percent", nullable = false, precision = 7, scale = 4)
    private BigDecimal differencePercent;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "result_lowest_price", joinColumns = @JoinColumn(name = "survey_item_id"))
    @Column(name = "store_id", nullable = false)
    private Set<UUID> lowestPriceStoreIds = new HashSet<>();
}

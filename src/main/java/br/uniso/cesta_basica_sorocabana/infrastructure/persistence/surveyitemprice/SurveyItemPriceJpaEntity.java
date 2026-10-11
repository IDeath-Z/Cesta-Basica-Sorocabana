package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveyitemprice;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "survey_item_price")
public class SurveyItemPriceJpaEntity {

    @EmbeddedId
    private SurveyItemPriceJpaId id;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}

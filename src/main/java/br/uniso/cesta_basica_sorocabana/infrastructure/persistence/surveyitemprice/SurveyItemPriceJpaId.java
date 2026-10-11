package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveyitemprice;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class SurveyItemPriceJpaId implements Serializable {

    @Column(name = "survey_item_id")
    private UUID surveyItemId;

    @Column(name = "store_id")
    private UUID storeId;
}

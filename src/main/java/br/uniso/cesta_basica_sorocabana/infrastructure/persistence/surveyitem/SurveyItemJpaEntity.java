package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveyitem;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand.ProductBrandJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.survey.SurveyJpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "survey_item")
public class SurveyItemJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false, foreignKey = @ForeignKey(name = "fk_survey_item_survey"))
    private SurveyJpaEntity survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_brand_id", nullable = false, foreignKey = @ForeignKey(name = "fk_survey_item_product_brand"))
    private ProductBrandJpaEntity productBrand;
}

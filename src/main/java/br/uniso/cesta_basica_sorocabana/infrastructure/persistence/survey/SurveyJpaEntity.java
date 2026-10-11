package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.survey;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveytype.SurveyTypeJpaEntity;

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
@Table(name = "survey")
public class SurveyJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "survey_date", nullable = false)
    private LocalDate surveyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_survey_survey_type"))
    private SurveyTypeJpaEntity surveyType;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "draft", nullable = false)
    private boolean draft;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}

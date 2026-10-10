package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.SurveyError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class Survey {

    private final UUID id;
    private final LocalDate surveyDate;
    private final UUID surveyTypeId;
    private final String notes;
    private final boolean draft;
    private final Instant createdAt;

    private Survey(UUID id, LocalDate surveyDate, UUID surveyTypeId, String notes, boolean draft, Instant createdAt) {
        this.id = DomainValidator.requireNonNull(id, SurveyError.ID_REQUIRED.message());
        this.surveyDate = DomainValidator.requireNonNull(surveyDate, SurveyError.SURVEY_DATE_REQUIRED.message());
        this.surveyTypeId = DomainValidator.requireNonNull(surveyTypeId, SurveyError.SURVEY_TYPE_ID_REQUIRED.message());
        this.notes = notes;
        this.draft = draft;
        this.createdAt = DomainValidator.requireNonNull(createdAt, SurveyError.CREATION_DATE_REQUIRED.message());
    }

    public static Survey createNew(LocalDate surveyDate, UUID surveyTypeId, String notes) {
        return new Survey(UUID.randomUUID(), surveyDate, surveyTypeId, notes, true, Instant.now());
    }

    public static Survey restore(UUID id, LocalDate surveyDate, UUID surveyTypeId, String notes, boolean draft,
        Instant createdAt) {
        return new Survey(id, surveyDate, surveyTypeId, notes, draft, createdAt);
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getSurveyDate() {
        return surveyDate;
    }

    public UUID getSurveyTypeId() {
        return surveyTypeId;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isDraft() {
        return draft;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Survey update(LocalDate surveyDate, UUID surveyTypeId, String notes) {
        return new Survey(
            this.id,
            surveyDate != null ? surveyDate : this.surveyDate,
            surveyTypeId != null ? surveyTypeId : this.surveyTypeId,
            notes != null ? notes : this.notes,
            this.draft,
            this.createdAt
        );
    }

    public Survey publish() {
        if (!this.draft)
            return this;

        return new Survey(this.id, this.surveyDate, this.surveyTypeId, this.notes, false, this.createdAt);
    }
}

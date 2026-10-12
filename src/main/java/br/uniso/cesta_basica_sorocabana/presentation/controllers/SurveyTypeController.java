package br.uniso.cesta_basica_sorocabana.presentation.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.uniso.cesta_basica_sorocabana.application.usecases.surveytype.UpdateSurveyTypeStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.surveytype.CreateSurveyTypeUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.surveytype.FetchSurveyTypeUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.surveytype.FetchSurveyTypesUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.surveytype.UpdateSurveyTypeUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.enums.SortBy;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityUserDetails;
import br.uniso.cesta_basica_sorocabana.presentation.dto.surveytype.SurveyTypeResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.surveytype.CreateSurveyTypeRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.surveytype.UpdateSurveyTypeRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.shared.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey-types")
@Tag(name = "Survey Types", description = "Kinds of price survey (e.g. weekly, Easter, Christmas). Reading requires only a valid token; writing requires the permission listed on each route.")
public class SurveyTypeController {

    private final CreateSurveyTypeUseCase createSurveyTypeUseCase;
    private final UpdateSurveyTypeUseCase updateSurveyTypeUseCase;
    private final FetchSurveyTypeUseCase fetchSurveyTypeUseCase;
    private final FetchSurveyTypesUseCase fetchSurveyTypesUseCase;
    private final UpdateSurveyTypeStatusUseCase updateSurveyTypeStatusUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping
    @Operation(summary = "List survey types", description = "Returns a page of survey types, active and inactive. Valid sortBy values: NAME_ASC, NAME_DESC.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of survey types"),
        @ApiResponse(responseCode = "400", description = "Invalid page, size or sortBy")
    })
    public ResponseEntity<PageResponse<SurveyTypeResponse>> list(
            @Parameter(description = "Page number, starting at 0", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Items per page, from 1 to 100", example = "20") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort order", example = "NAME_ASC") @RequestParam(defaultValue = "NAME_ASC") SortBy sortBy) {

        var output = transactionalProxy.executeReadOnly(() -> fetchSurveyTypesUseCase.execute(
            new FetchSurveyTypesUseCase.Input(new DomainPageRequest(page, size, sortBy))
        ));

        return ResponseEntity.ok(PageResponse.from(output, SurveyTypeResponse::from));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a survey type", description = "Returns a single survey type by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Survey type found"),
        @ApiResponse(responseCode = "404", description = "Survey type not found")
    })
    public ResponseEntity<SurveyTypeResponse> get(
            @Parameter(description = "Survey type ID", example = "c2e8f6a1-4b3d-4e7f-9a0b-5d6c7e8f9a1b") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchSurveyTypeUseCase.execute(new FetchSurveyTypeUseCase.Input(id)));

        return ResponseEntity.ok(SurveyTypeResponse.from(output));
    }

    @PostMapping
    @RequiresPermission(Permission.CREATE_SURVEY_TYPE)
    @Operation(summary = "Create a survey type", description = "Registers a new active survey type. Requires permission CREATE_SURVEY_TYPE.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Survey type created"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "409", description = "A survey type with this code already exists")
    })
    public ResponseEntity<SurveyTypeResponse> create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateSurveyTypeRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createSurveyTypeUseCase.execute(new CreateSurveyTypeUseCase.Input(requesterId, request.code(), request.name(), request.description())));

        return ResponseEntity.status(HttpStatus.CREATED).body(SurveyTypeResponse.from(output));
    }

    @PatchMapping("/{id}")
    @RequiresPermission(Permission.UPDATE_SURVEY_TYPE)
    @Operation(summary = "Update a survey type", description = "Changes the code, name and/or description of an existing survey type. Omitted fields keep their value. Requires permission UPDATE_SURVEY_TYPE.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Survey type updated"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "404", description = "Survey type not found"),
        @ApiResponse(responseCode = "409", description = "Another survey type already has this code")
    })
    public ResponseEntity<SurveyTypeResponse> update(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Survey type ID", example = "c2e8f6a1-4b3d-4e7f-9a0b-5d6c7e8f9a1b") @PathVariable UUID id,
            @RequestBody @Valid UpdateSurveyTypeRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateSurveyTypeUseCase.execute(new UpdateSurveyTypeUseCase.Input(requesterId, id, request.code(), request.name(), request.description())));

        return ResponseEntity.ok(SurveyTypeResponse.from(output));
    }

    @PatchMapping("/{id}/activate")
    @RequiresPermission(Permission.UPDATE_SURVEY_TYPE)
    @Operation(summary = "Activate a survey type", description = "Marks the survey type as active again. Does nothing if it is already active. Requires permission UPDATE_SURVEY_TYPE.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Survey type is active"),
        @ApiResponse(responseCode = "404", description = "Survey type not found")
    })
    public ResponseEntity<SurveyTypeResponse> activate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Survey type ID", example = "c2e8f6a1-4b3d-4e7f-9a0b-5d6c7e8f9a1b") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateSurveyTypeStatusUseCase.execute(new UpdateSurveyTypeStatusUseCase.Input(requesterId, id, true)));

        return ResponseEntity.ok(SurveyTypeResponse.from(output));
    }

    @PatchMapping("/{id}/deactivate")
    @RequiresPermission(Permission.UPDATE_SURVEY_TYPE)
    @Operation(summary = "Deactivate a survey type", description = "Marks the survey type as inactive so it can no longer be used by new surveys. Existing records keep it. Requires permission UPDATE_SURVEY_TYPE.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Survey type is inactive"),
        @ApiResponse(responseCode = "404", description = "Survey type not found")
    })
    public ResponseEntity<SurveyTypeResponse> deactivate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Survey type ID", example = "c2e8f6a1-4b3d-4e7f-9a0b-5d6c7e8f9a1b") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateSurveyTypeStatusUseCase.execute(new UpdateSurveyTypeStatusUseCase.Input(requesterId, id, false)));

        return ResponseEntity.ok(SurveyTypeResponse.from(output));
    }
}

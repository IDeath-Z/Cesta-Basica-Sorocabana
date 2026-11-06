package br.uniso.cesta_basica_sorocabana.presentation.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.uniso.cesta_basica_sorocabana.application.usecases.survey.CreateSurveyUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.survey.DeleteSurveyUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.survey.FetchSurveyUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.survey.FetchSurveysUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.survey.SaveSurveyPricesUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.survey.UpdateSurveyUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.enums.SortBy;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityUserDetails;
import br.uniso.cesta_basica_sorocabana.presentation.dto.shared.PageResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.survey.CreateSurveyRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.survey.SaveSurveyPricesRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.survey.SurveyDetailResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.survey.SurveyResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.survey.UpdateSurveyRequest;

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
@RequestMapping("/surveys")
@Tag(name = "Surveys", description = "Price surveys. A survey is created as a draft and receives the prices of each store. While it is a draft it can be edited and deleted. Reading requires only a valid token; writing requires the permission listed on each route.")
public class SurveyController {

    private final CreateSurveyUseCase createSurveyUseCase;
    private final FetchSurveysUseCase fetchSurveysUseCase;
    private final FetchSurveyUseCase fetchSurveyUseCase;
    private final UpdateSurveyUseCase updateSurveyUseCase;
    private final DeleteSurveyUseCase deleteSurveyUseCase;
    private final SaveSurveyPricesUseCase saveSurveyPricesUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping
    @Operation(summary = "List surveys", description = "Returns a page of surveys, drafts included, without their items. Valid sortBy values: SURVEY_DATE_ASC, SURVEY_DATE_DESC, CREATED_AT_ASC, CREATED_AT_DESC.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of surveys"),
        @ApiResponse(responseCode = "400", description = "Invalid page, size or sortBy")
    })
    public ResponseEntity<PageResponse<SurveyResponse>> list(
            @Parameter(description = "Page number, starting at 0", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Items per page, from 1 to 100", example = "20") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort order", example = "SURVEY_DATE_DESC") @RequestParam(defaultValue = "SURVEY_DATE_DESC") SortBy sortBy) {

        var output = transactionalProxy.executeReadOnly(() -> fetchSurveysUseCase.execute(
            new FetchSurveysUseCase.Input(new DomainPageRequest(page, size, sortBy))
        ));

        return ResponseEntity.ok(PageResponse.from(output, SurveyResponse::from));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a survey", description = "Returns the survey with the stores that already have prices and every item saved in each.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Survey found"),
        @ApiResponse(responseCode = "404", description = "Survey not found")
    })
    public ResponseEntity<SurveyDetailResponse> get(
            @Parameter(description = "Survey ID", example = "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchSurveyUseCase.execute(new FetchSurveyUseCase.Input(id)));

        return ResponseEntity.ok(SurveyDetailResponse.from(output));
    }

    @PostMapping
    @RequiresPermission(Permission.CREATE_SURVEY)
    @Operation(summary = "Create a survey", description = "Creates a draft survey, still without items. Requires permission CREATE_SURVEY.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Draft survey created"),
        @ApiResponse(responseCode = "400", description = "Invalid payload, or the survey type is inactive"),
        @ApiResponse(responseCode = "404", description = "Survey type not found"),
        @ApiResponse(responseCode = "409", description = "A survey with this date and type already exists")
    })
    public ResponseEntity<SurveyResponse> create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateSurveyRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createSurveyUseCase.execute(
            new CreateSurveyUseCase.Input(requesterId, request.surveyDate(), request.surveyTypeId(), request.notes())
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(SurveyResponse.from(output));
    }

    @PatchMapping("/{id}")
    @RequiresPermission(Permission.UPDATE_SURVEY)
    @Operation(summary = "Update a draft survey", description = "Changes the date, type and/or notes. Omitted fields keep their value. Prices are changed by POST /surveys/{id}/prices. Requires permission UPDATE_SURVEY.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Survey updated"),
        @ApiResponse(responseCode = "400", description = "Invalid payload, inactive survey type, or the survey is no longer a draft"),
        @ApiResponse(responseCode = "404", description = "Survey or survey type not found"),
        @ApiResponse(responseCode = "409", description = "Another survey already has this date and type")
    })
    public ResponseEntity<SurveyResponse> update(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Survey ID", example = "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d") @PathVariable UUID id,
            @RequestBody @Valid UpdateSurveyRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateSurveyUseCase.execute(
            new UpdateSurveyUseCase.Input(requesterId, id, request.surveyDate(), request.surveyTypeId(), request.notes())
        ));

        return ResponseEntity.ok(SurveyResponse.from(output));
    }

    @DeleteMapping("/{id}")
    @RequiresPermission(Permission.DELETE_SURVEY)
    @Operation(summary = "Delete a draft survey", description = "Deletes the draft and everything saved in it. The audit keeps a copy. Requires permission DELETE_SURVEY.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Survey deleted"),
        @ApiResponse(responseCode = "400", description = "The survey is no longer a draft"),
        @ApiResponse(responseCode = "404", description = "Survey not found")
    })
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Survey ID", example = "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        transactionalProxy.execute(() -> deleteSurveyUseCase.execute(new DeleteSurveyUseCase.Input(requesterId, id)));

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/prices")
    @RequiresPermission(Permission.REGISTER_SURVEY_ITEM_PRICE)
    @Operation(summary = "Save prices", description = "Saves prices of one or more stores in one transaction: an invalid line saves nothing. Send one item or the whole screen; items not sent stay as they are, and a null price clears the item in that store. Returns the survey as saved. Requires permission REGISTER_SURVEY_ITEM_PRICE.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Prices saved"),
        @ApiResponse(responseCode = "400", description = "Invalid payload, item not active in the store, item sent twice, or the survey is no longer a draft"),
        @ApiResponse(responseCode = "404", description = "Survey not found")
    })
    public ResponseEntity<SurveyDetailResponse> savePrices(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Survey ID", example = "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d") @PathVariable UUID id,
            @RequestBody @Valid SaveSurveyPricesRequest request) {
        UUID requesterId = userDetails.getUser().getId();
        List<SaveSurveyPricesUseCase.StoreInput> stores = request.stores().stream()
            .map(store -> new SaveSurveyPricesUseCase.StoreInput(store.storeId(), store.items().stream()
                .map(item -> new SaveSurveyPricesUseCase.ItemInput(item.productBrandId(), item.price()))
                .toList()))
            .toList();

        var output = transactionalProxy.execute(() -> saveSurveyPricesUseCase.execute(
            new SaveSurveyPricesUseCase.Input(requesterId, id, stores)
        ));

        return ResponseEntity.ok(SurveyDetailResponse.from(output));
    }
}

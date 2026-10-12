package br.uniso.cesta_basica_sorocabana.presentation.dto.shared;

import java.util.List;
import java.util.function.Function;

import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PageResponse", description = "A page of results")
public record PageResponse<T>(
    @Schema(description = "Items of the current page")
    List<T> content,

    @Schema(description = "Current page number, starting at 0", example = "0")
    int currentPage,

    @Schema(description = "Maximum number of items per page", example = "20")
    int pageSize,

    @Schema(description = "Total number of items across all pages", example = "42")
    long totalElements,

    @Schema(description = "Total number of pages", example = "3")
    int totalPages
) {
    public static <S, T> PageResponse<T> from(DomainPageResult<S> page, Function<S, T> mapper) {
        return new PageResponse<>(
            page.content().stream().map(mapper).toList(),
            page.page(),
            page.size(),
            page.totalElements(),
            page.totalPages()
        );
    }
}

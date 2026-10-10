package br.uniso.cesta_basica_sorocabana.domain.shared;

import java.util.List;
import java.util.function.Function;

public record DomainPageResult<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
    public <R> DomainPageResult<R> map(Function<T, R> mapper) {
        return new DomainPageResult<>(content.stream().map(mapper).toList(), page, size, totalElements, totalPages);
    }
}

package br.uniso.cesta_basica_sorocabana.domain.shared;

import br.uniso.cesta_basica_sorocabana.domain.enums.SortBy;

public record DomainPageRequest(
    int page,
    int size,
    SortBy sortBy
) {}

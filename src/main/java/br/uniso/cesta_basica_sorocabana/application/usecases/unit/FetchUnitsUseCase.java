package br.uniso.cesta_basica_sorocabana.application.usecases.unit;

import br.uniso.cesta_basica_sorocabana.domain.ports.UnitRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchUnitsUseCase {

    private final UnitRepository unitRepository;

    public record Input(DomainPageRequest pageRequest) {}

    public DomainPageResult<UnitOutput> execute(Input input) {
        return unitRepository.findAll(input.pageRequest()).map(UnitOutput::from);
    }
}

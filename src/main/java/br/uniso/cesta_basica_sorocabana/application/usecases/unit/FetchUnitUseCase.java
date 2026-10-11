package br.uniso.cesta_basica_sorocabana.application.usecases.unit;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.UnitApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.UnitRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchUnitUseCase {

    private final UnitRepository unitRepository;

    public record Input(UUID id) {}

    public UnitOutput execute(Input input) {
        return unitRepository.findById(input.id())
            .map(UnitOutput::from)
            .orElseThrow(() -> new ResourceNotFoundException(UnitApplicationError.NOT_FOUND.message(input.id())));
    }
}

package br.uniso.cesta_basica_sorocabana.application.usecases.brand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.BrandApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.BrandRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchBrandUseCase {

    private final BrandRepository brandRepository;

    public record Input(UUID id) {}

    public BrandOutput execute(Input input) {
        return brandRepository.findById(input.id())
            .map(BrandOutput::from)
            .orElseThrow(() -> new ResourceNotFoundException(BrandApplicationError.NOT_FOUND.message(input.id())));
    }
}

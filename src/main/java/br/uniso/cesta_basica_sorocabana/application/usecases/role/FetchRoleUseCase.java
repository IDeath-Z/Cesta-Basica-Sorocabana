package br.uniso.cesta_basica_sorocabana.application.usecases.role;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.RoleApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchRoleUseCase {

    private final RoleRepository roleRepository;

    public record Input(UUID id) {}

    public RoleOutput execute(Input input) {
        return roleRepository.findById(input.id())
            .map(RoleOutput::from)
            .orElseThrow(() -> new ResourceNotFoundException(RoleApplicationError.NOT_FOUND.message(input.id())));
    }
}

package br.uniso.cesta_basica_sorocabana.application.usecases.role;

import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchRolesUseCase {

    private final RoleRepository roleRepository;

    public record Input(DomainPageRequest pageRequest) {}

    public DomainPageResult<RoleOutput> execute(Input input) {
        return roleRepository.findAll(input.pageRequest()).map(RoleOutput::from);
    }
}

package br.uniso.cesta_basica_sorocabana.application.usecases.user;

import br.uniso.cesta_basica_sorocabana.domain.ports.UserRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchUsersUseCase {

    private final UserRepository userRepository;

    public record Input(DomainPageRequest pageRequest) {}

    public DomainPageResult<UserOutput> execute(Input input) {
        return userRepository.findAll(input.pageRequest()).map(UserOutput::from);
    }
}

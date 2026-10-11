package br.uniso.cesta_basica_sorocabana.application.usecases.user;

import java.util.Set;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.UserApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.entities.Role;
import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchCurrentUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public record Input(UUID userId) {}

    public record Output(UUID id, String email, UUID roleId, String roleName, Set<Permission> permissions) {}

    // An inactive role grants no permission, so the list comes back empty in that case.
    public Output execute(Input input) {
        User user = userRepository.findById(input.userId())
            .orElseThrow(() -> new ResourceNotFoundException(UserApplicationError.NOT_FOUND.message(input.userId())));

        Role role = roleRepository.findById(user.getRoleId()).orElse(null);
        Set<Permission> permissions = role != null && role.isActive() ? role.getPermissions() : Set.of();

        return new Output(user.getId(), user.getEmail(), user.getRoleId(), role != null ? role.getName() : null, permissions);
    }
}

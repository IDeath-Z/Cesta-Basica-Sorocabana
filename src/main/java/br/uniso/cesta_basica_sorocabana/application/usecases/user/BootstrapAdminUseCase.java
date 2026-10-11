package br.uniso.cesta_basica_sorocabana.application.usecases.user;

import java.util.Set;

import br.uniso.cesta_basica_sorocabana.domain.entities.Role;
import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.ports.PasswordHasher;
import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

// Runs on every startup: the admin role always ends up active and with every permission (including new ones),
// and the admin user is created only once, so a changed password is never overwritten.
@RequiredArgsConstructor
public final class BootstrapAdminUseCase {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public record Input(String roleName, String email, String password) {}

    public record Output(boolean userCreated) {}

    public Output execute(Input input) {
        Set<Permission> allPermissions = Set.of(Permission.values());

        Role role = roleRepository.findByName(input.roleName())
            .map(existing -> existing.update(null, allPermissions).activate())
            .orElseGet(() -> Role.createNew(input.roleName(), allPermissions));
        Role savedRole = roleRepository.save(role);

        if (userRepository.existsByEmail(input.email()))
            return new Output(false);

        userRepository.save(User.createNew(input.email(), passwordHasher.hash(input.password()), savedRole.getId()));
        return new Output(true);
    }
}

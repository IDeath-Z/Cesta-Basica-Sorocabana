package br.uniso.cesta_basica_sorocabana.infrastructure.security;

import java.util.Set;

import br.uniso.cesta_basica_sorocabana.domain.entities.Role;
import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.UserRepository;
import br.uniso.cesta_basica_sorocabana.infrastructure.enums.errors.AuthError;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(AuthError.USER_NOT_FOUND.message(email)));
        return new SecurityUserDetails(user, loadPermissions(user));
    }

    // An inactive role grants no permission.
    private Set<Permission> loadPermissions(User user) {
        return roleRepository.findById(user.getRoleId())
            .filter(Role::isActive)
            .map(Role::getPermissions)
            .orElse(Set.of());
    }
}

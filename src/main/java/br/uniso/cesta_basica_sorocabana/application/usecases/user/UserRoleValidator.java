package br.uniso.cesta_basica_sorocabana.application.usecases.user;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.RoleApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.BusinessRuleException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.entities.Role;
import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;

import lombok.RequiredArgsConstructor;

// A user can only be assigned to an existing, active role.
@RequiredArgsConstructor
public final class UserRoleValidator {

    private final RoleRepository roleRepository;

    public void requireActiveRole(UUID roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException(RoleApplicationError.NOT_FOUND.message(roleId)));

        if (!role.isActive())
            throw new BusinessRuleException(RoleApplicationError.INACTIVE.message(role.getName()));
    }
}

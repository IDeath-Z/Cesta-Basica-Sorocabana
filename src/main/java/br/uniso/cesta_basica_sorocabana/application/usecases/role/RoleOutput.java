package br.uniso.cesta_basica_sorocabana.application.usecases.role;

import java.util.Set;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Role;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;

public record RoleOutput(UUID id, String name, boolean active, Set<Permission> permissions) {

    public static RoleOutput from(Role role) {
        return new RoleOutput(role.getId(), role.getName(), role.isActive(), role.getPermissions());
    }
}

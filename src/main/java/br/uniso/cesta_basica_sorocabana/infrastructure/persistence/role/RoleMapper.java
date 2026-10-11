package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.role;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.Role;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.permission.PermissionJpaEntity;

@Component
public final class RoleMapper {

    public RoleJpaEntity toJpa(Role role, Set<PermissionJpaEntity> permissions) {
        RoleJpaEntity entity = new RoleJpaEntity();
        entity.setId(role.getId());
        entity.setName(role.getName());
        entity.setActive(role.isActive());
        entity.setPermissions(permissions);
        return entity;
    }

    // Inactive rows are permissions removed from the enum, so they are ignored.
    public Role toDomain(RoleJpaEntity entity) {
        Set<Permission> permissions = entity.getPermissions().stream()
            .filter(PermissionJpaEntity::isActive)
            .map(permission -> Permission.valueOf(permission.getCode()))
            .collect(Collectors.toSet());

        return Role.restore(
            entity.getId(),
            entity.getName(),
            entity.isActive(),
            permissions
        );
    }
}

package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.util.Set;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.enums.errors.RoleError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class Role {

    private final UUID id;
    private final String name;
    private final boolean active;
    private final Set<Permission> permissions;

    private Role(UUID id, String name, boolean active, Set<Permission> permissions) {
        this.id = DomainValidator.requireNonNull(id, RoleError.ID_REQUIRED.message());
        this.name = DomainValidator.requireNonBlank(name, RoleError.NAME_REQUIRED.message());
        this.active = active;
        this.permissions = permissions == null ? Set.of() : Set.copyOf(permissions);
    }

    public static Role createNew(String name, Set<Permission> permissions) {
        return new Role(UUID.randomUUID(), name, true, permissions);
    }

    public static Role restore(UUID id, String name, boolean active, Set<Permission> permissions) {
        return new Role(id, name, active, permissions);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(Permission permission) {
        return this.active && this.permissions.contains(permission);
    }

    public Role update(String name, Set<Permission> permissions) {
        return new Role(
            this.id,
            name != null ? name : this.name,
            this.active,
            permissions != null ? permissions : this.permissions
        );
    }

    public Role activate() {
        if (this.active)
            return this;

        return new Role(this.id, this.name, true, this.permissions);
    }

    public Role deactivate() {
        if (!this.active)
            return this;

        return new Role(this.id, this.name, false, this.permissions);
    }
}

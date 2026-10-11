package br.uniso.cesta_basica_sorocabana.application.usecases.role;

import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;

public record PermissionOutput(Permission code, String description) {

    public static PermissionOutput from(Permission permission) {
        return new PermissionOutput(permission, permission.getDescription());
    }
}

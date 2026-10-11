package br.uniso.cesta_basica_sorocabana.application.usecases.role;

import java.util.Arrays;
import java.util.List;

import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;

// Permissions come only from the code, so they are listed straight from the enum.
public final class FetchPermissionsUseCase {

    public List<PermissionOutput> execute() {
        return Arrays.stream(Permission.values())
            .map(PermissionOutput::from)
            .toList();
    }
}

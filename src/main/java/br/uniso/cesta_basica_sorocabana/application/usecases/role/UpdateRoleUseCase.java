package br.uniso.cesta_basica_sorocabana.application.usecases.role;

import java.util.Set;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.RoleApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ForbiddenActionException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Role;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateRoleUseCase {

    private final RoleRepository roleRepository;
    private final AuditRecorder auditRecorder;
    private final String protectedRoleName;

    // permissions replaces the whole set when provided; null keeps the current one.
    public record Input(UUID requesterId, UUID id, String name, Set<Permission> permissions) {}

    // The admin role always keeps every permission, so it cannot be edited.
    public RoleOutput execute(Input input) {
        Role role = roleRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(RoleApplicationError.NOT_FOUND.message(input.id())));

        if (role.getName().equals(protectedRoleName))
            throw new ForbiddenActionException(RoleApplicationError.PROTECTED.message(role.getName()));

        if (input.name() != null && !input.name().equals(role.getName()) && roleRepository.existsByName(input.name()))
            throw new ResourceAlreadyExistsException(RoleApplicationError.NAME_ALREADY_EXISTS.message(input.name()));

        RoleOutput before = RoleOutput.from(role);
        RoleOutput after = RoleOutput.from(roleRepository.save(role.update(input.name(), input.permissions())));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.ROLE, after.id(), before, after);
        return after;
    }
}

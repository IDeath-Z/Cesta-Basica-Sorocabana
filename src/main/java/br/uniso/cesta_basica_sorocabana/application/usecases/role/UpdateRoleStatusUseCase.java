package br.uniso.cesta_basica_sorocabana.application.usecases.role;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.RoleApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ForbiddenActionException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Role;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateRoleStatusUseCase {

    private final RoleRepository roleRepository;
    private final AuditRecorder auditRecorder;
    private final String protectedRoleName;

    public record Input(UUID requesterId, UUID id, boolean active) {}

    // The admin role can never be deactivated. Already in the requested state: nothing is saved or audited.
    public RoleOutput execute(Input input) {
        Role role = roleRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(RoleApplicationError.NOT_FOUND.message(input.id())));

        if (role.getName().equals(protectedRoleName))
            throw new ForbiddenActionException(RoleApplicationError.PROTECTED.message(role.getName()));

        Role changed = input.active() ? role.activate() : role.deactivate();
        if (changed == role)
            return RoleOutput.from(role);

        RoleOutput before = RoleOutput.from(role);
        RoleOutput after = RoleOutput.from(roleRepository.save(changed));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.ROLE, after.id(), before, after);
        return after;
    }
}

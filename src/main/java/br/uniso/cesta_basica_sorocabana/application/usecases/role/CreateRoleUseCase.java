package br.uniso.cesta_basica_sorocabana.application.usecases.role;

import java.util.Set;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.RoleApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Role;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateRoleUseCase {

    private final RoleRepository roleRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, String name, Set<Permission> permissions) {}

    public RoleOutput execute(Input input) {
        if (roleRepository.existsByName(input.name()))
            throw new ResourceAlreadyExistsException(RoleApplicationError.NAME_ALREADY_EXISTS.message(input.name()));

        RoleOutput output = RoleOutput.from(roleRepository.save(Role.createNew(input.name(), input.permissions())));

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.ROLE, output.id(), output);
        return output;
    }
}

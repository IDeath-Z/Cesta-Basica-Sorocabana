package br.uniso.cesta_basica_sorocabana.application.usecases.user;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.UserApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ChangeUserRoleUseCase {

    private final UserRepository userRepository;
    private final UserRoleValidator roleValidator;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, UUID roleId) {}

    public UserOutput execute(Input input) {
        User user = userRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(UserApplicationError.NOT_FOUND.message(input.id())));

        roleValidator.requireActiveRole(input.roleId());

        UserOutput before = UserOutput.from(user);
        UserOutput after = UserOutput.from(userRepository.save(user.changeRole(input.roleId())));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.USER, after.id(), before, after);
        return after;
    }
}

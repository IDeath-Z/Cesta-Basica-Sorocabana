package br.uniso.cesta_basica_sorocabana.application.usecases.user;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.UserApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.BusinessRuleException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateUserStatusUseCase {

    private final UserRepository userRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, boolean active) {}

    // Already in the requested state: nothing changes, so nothing is saved or audited.
    public UserOutput execute(Input input) {
        if (!input.active() && input.requesterId().equals(input.id()))
            throw new BusinessRuleException(UserApplicationError.CANNOT_DEACTIVATE_SELF.message());

        User user = userRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(UserApplicationError.NOT_FOUND.message(input.id())));

        User changed = input.active() ? user.activate() : user.deactivate();
        if (changed == user)
            return UserOutput.from(user);

        UserOutput before = UserOutput.from(user);
        UserOutput after = UserOutput.from(userRepository.save(changed));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.USER, after.id(), before, after);
        return after;
    }
}

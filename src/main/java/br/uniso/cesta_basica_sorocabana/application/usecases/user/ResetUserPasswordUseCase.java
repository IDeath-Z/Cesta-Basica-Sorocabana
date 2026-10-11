package br.uniso.cesta_basica_sorocabana.application.usecases.user;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.UserApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.PasswordHasher;
import br.uniso.cesta_basica_sorocabana.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

// An admin sets a new password for another user (e.g. a forgotten one), without the current password.
@RequiredArgsConstructor
public final class ResetUserPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, String newPassword) {}

    public void execute(Input input) {
        User user = userRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(UserApplicationError.NOT_FOUND.message(input.id())));

        userRepository.save(user.updatePassword(passwordHasher.hash(input.newPassword())));

        PasswordAuditSnapshot snapshot = new PasswordAuditSnapshot(user.getId(), user.getEmail());
        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.USER_PASSWORD, user.getId(), snapshot, snapshot);
    }
}

package br.uniso.cesta_basica_sorocabana.application.usecases.user;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.UserApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.BusinessRuleException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.PasswordHasher;
import br.uniso.cesta_basica_sorocabana.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

// The logged user changes their own password, proving they know the current one.
@RequiredArgsConstructor
public final class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final AuditRecorder auditRecorder;

    public record Input(UUID userId, String currentPassword, String newPassword) {}

    public void execute(Input input) {
        User user = userRepository.findById(input.userId())
            .orElseThrow(() -> new ResourceNotFoundException(UserApplicationError.NOT_FOUND.message(input.userId())));

        if (!passwordHasher.verify(input.currentPassword(), user.getPasswordHash()))
            throw new BusinessRuleException(UserApplicationError.WRONG_CURRENT_PASSWORD.message());

        userRepository.save(user.updatePassword(passwordHasher.hash(input.newPassword())));

        PasswordAuditSnapshot snapshot = new PasswordAuditSnapshot(user.getId(), user.getEmail());
        auditRecorder.recordUpdate(user.getId(), AuditedEntity.USER_PASSWORD, user.getId(), snapshot, snapshot);
    }
}

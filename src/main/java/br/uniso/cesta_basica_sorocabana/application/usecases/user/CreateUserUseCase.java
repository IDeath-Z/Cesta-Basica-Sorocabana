package br.uniso.cesta_basica_sorocabana.application.usecases.user;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.UserApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.PasswordHasher;
import br.uniso.cesta_basica_sorocabana.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final UserRoleValidator roleValidator;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, String email, String password, UUID roleId) {}

    public UserOutput execute(Input input) {
        if (userRepository.existsByEmail(input.email()))
            throw new ResourceAlreadyExistsException(UserApplicationError.EMAIL_ALREADY_REGISTERED.message(input.email()));

        roleValidator.requireActiveRole(input.roleId());

        User user = User.createNew(input.email(), passwordHasher.hash(input.password()), input.roleId());
        UserOutput output = UserOutput.from(userRepository.save(user));

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.USER, output.id(), output);
        return output;
    }
}

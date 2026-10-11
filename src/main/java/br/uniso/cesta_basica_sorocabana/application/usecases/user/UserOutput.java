package br.uniso.cesta_basica_sorocabana.application.usecases.user;

import java.time.Instant;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.User;

// Never exposes the password hash.
public record UserOutput(UUID id, String email, UUID roleId, boolean active, Instant createdAt) {

    public static UserOutput from(User user) {
        return new UserOutput(user.getId(), user.getEmail(), user.getRoleId(), user.isActive(), user.getCreatedAt());
    }
}

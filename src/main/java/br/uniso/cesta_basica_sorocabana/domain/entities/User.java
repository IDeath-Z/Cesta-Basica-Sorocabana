package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.time.Instant;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.UserError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class User {

    private final UUID id;
    private final String email;
    private final String passwordHash;
    private final UUID roleId;
    private final boolean active;
    private final Instant createdAt;

    private User(UUID id, String email, String passwordHash, UUID roleId, boolean active, Instant createdAt) {
        this.id = DomainValidator.requireNonNull(id, UserError.ID_REQUIRED.message());
        this.email = DomainValidator.requireNonBlank(email, UserError.EMAIL_REQUIRED.message()).trim().toLowerCase();
        this.passwordHash = DomainValidator.requireNonBlank(passwordHash, UserError.PASSWORD_REQUIRED.message());
        this.roleId = DomainValidator.requireNonNull(roleId, UserError.ROLE_ID_REQUIRED.message());
        this.active = active;
        this.createdAt = DomainValidator.requireNonNull(createdAt, UserError.CREATION_DATE_REQUIRED.message());
    }

    public static User createNew(String email, String passwordHash, UUID roleId) {
        return new User(UUID.randomUUID(), email, passwordHash, roleId, true, Instant.now());
    }

    public static User restore(UUID id, String email, String passwordHash, UUID roleId, boolean active,
        Instant createdAt) {
        return new User(id, email, passwordHash, roleId, active, createdAt);
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public User changeRole(UUID roleId) {
        return new User(this.id, this.email, this.passwordHash, roleId, this.active, this.createdAt);
    }

    public User updatePassword(String newPasswordHash) {
        return new User(this.id, this.email, newPasswordHash, this.roleId, this.active, this.createdAt);
    }

    public User activate() {
        if (this.active)
            return this;

        return new User(this.id, this.email, this.passwordHash, this.roleId, true, this.createdAt);
    }

    public User deactivate() {
        if (!this.active)
            return this;

        return new User(this.id, this.email, this.passwordHash, this.roleId, false, this.createdAt);
    }
}

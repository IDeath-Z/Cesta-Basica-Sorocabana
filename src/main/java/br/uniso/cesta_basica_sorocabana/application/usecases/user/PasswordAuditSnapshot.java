package br.uniso.cesta_basica_sorocabana.application.usecases.user;

import java.util.UUID;

// What the audit keeps about a password change: whose password changed, never the password or its hash.
public record PasswordAuditSnapshot(UUID userId, String email) {}

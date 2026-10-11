package br.uniso.cesta_basica_sorocabana.application.usecases.audit;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.AuditApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.AuditRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchAuditUseCase {

    private final AuditRepository auditRepository;

    public record Input(UUID id) {}

    public AuditOutput execute(Input input) {
        return auditRepository.findById(input.id())
            .map(AuditOutput::from)
            .orElseThrow(() -> new ResourceNotFoundException(AuditApplicationError.NOT_FOUND.message(input.id())));
    }
}

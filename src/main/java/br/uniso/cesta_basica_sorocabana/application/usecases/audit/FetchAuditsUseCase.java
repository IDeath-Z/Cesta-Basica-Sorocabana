package br.uniso.cesta_basica_sorocabana.application.usecases.audit;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.AuditRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.AuditFilter;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchAuditsUseCase {

    private final AuditRepository auditRepository;

    // Every filter is optional and they combine: null means "any" (a null entity lists every entity).
    public record Input(UUID userId, AuditedEntity entity, UUID entityId, DomainPageRequest pageRequest) {}

    public DomainPageResult<AuditOutput> execute(Input input) {
        AuditFilter filter = new AuditFilter(input.userId(), input.entity(), input.entityId());
        return auditRepository.search(filter, input.pageRequest()).map(AuditOutput::from);
    }
}

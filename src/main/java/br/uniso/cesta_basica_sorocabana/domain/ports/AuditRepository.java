package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Audit;
import br.uniso.cesta_basica_sorocabana.domain.shared.AuditFilter;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

public interface AuditRepository {

    Audit save(Audit audit);
    Optional<Audit> findById(UUID id);
    DomainPageResult<Audit> search(AuditFilter filter, DomainPageRequest pageRequest);
}

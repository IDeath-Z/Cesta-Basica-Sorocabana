package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.audit;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.Audit;
import br.uniso.cesta_basica_sorocabana.domain.ports.AuditRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.AuditFilter;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.PageConverter;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.user.UserJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.user.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class AuditRepositoryAdapter implements AuditRepository {

    private final AuditJpaRepository auditJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final AuditMapper auditMapper;

    @Override
    public Audit save(Audit audit) {
        UserJpaEntity userProxy = userJpaRepository.getReferenceById(audit.getUserId());

        AuditJpaEntity entity = auditMapper.toJpa(audit, userProxy);
        return auditMapper.toDomain(auditJpaRepository.save(entity));
    }

    @Override
    public Optional<Audit> findById(UUID id) {
        return auditJpaRepository.findById(id).map(auditMapper::toDomain);
    }

    @Override
    public DomainPageResult<Audit> search(AuditFilter filter, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<AuditJpaEntity> page = auditJpaRepository.findAll(AuditSpecifications.from(filter), springPage);

        return PageConverter.toDomain(page, auditMapper::toDomain);
    }
}

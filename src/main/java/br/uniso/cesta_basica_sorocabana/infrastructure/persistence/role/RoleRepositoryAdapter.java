package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.role;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.Role;
import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.permission.PermissionJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.permission.PermissionJpaRepository;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.PageConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final RoleMapper roleMapper;

    @Override
    public Role save(Role role) {
        Set<PermissionJpaEntity> permissionProxies = role.getPermissions().stream()
            .map(permission -> permissionJpaRepository.getReferenceById(permission.name()))
            .collect(Collectors.toSet());

        RoleJpaEntity entity = roleMapper.toJpa(role, permissionProxies);
        return roleMapper.toDomain(roleJpaRepository.save(entity));
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleJpaRepository.findWithPermissionsById(id).map(roleMapper::toDomain);
    }

    @Override
    public DomainPageResult<Role> findAll(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<RoleJpaEntity> page = roleJpaRepository.findAll(springPage);

        return PageConverter.toDomain(page, roleMapper::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name).map(roleMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }
}

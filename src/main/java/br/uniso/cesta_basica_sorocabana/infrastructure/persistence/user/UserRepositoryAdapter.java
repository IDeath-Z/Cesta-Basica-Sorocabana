package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.ports.UserRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.role.RoleJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.role.RoleJpaRepository;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.PageConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        RoleJpaEntity roleProxy = roleJpaRepository.getReferenceById(user.getRoleId());

        UserJpaEntity entity = userMapper.toJpa(user, roleProxy);
        return userMapper.toDomain(userJpaRepository.save(entity));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public DomainPageResult<User> findAll(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<UserJpaEntity> page = userJpaRepository.findAll(springPage);

        return PageConverter.toDomain(page, userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email.trim().toLowerCase()).map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email.trim().toLowerCase());
    }
}

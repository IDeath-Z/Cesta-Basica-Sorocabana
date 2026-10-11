package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.user;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.role.RoleJpaEntity;

@Component
public final class UserMapper {

    public UserJpaEntity toJpa(User user, RoleJpaEntity roleJpaEntity) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setRole(roleJpaEntity);
        entity.setActive(user.isActive());
        entity.setCreatedAt(user.getCreatedAt());
        return entity;
    }

    public User toDomain(UserJpaEntity entity) {
        return User.restore(
            entity.getId(),
            entity.getEmail(),
            entity.getPasswordHash(),
            entity.getRole().getId(),
            entity.isActive(),
            entity.getCreatedAt()
        );
    }
}

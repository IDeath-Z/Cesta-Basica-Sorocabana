package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.permission;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Permissions only come from the code: on startup, every Permission constant is written to the permission table.
// Rows whose constant was removed from the enum are deactivated, not deleted, because roles may still reference them.
@Slf4j
@Component
@RequiredArgsConstructor
public final class PermissionSynchronizer {

    private final PermissionJpaRepository permissionJpaRepository;
    private final TransactionalProxy transactionalProxy;

    // Runs before AdminBootstrap, which needs every permission row to exist.
    @Order(1)
    @EventListener(ApplicationReadyEvent.class)
    public void synchronize() {
        transactionalProxy.execute(() -> {
            Map<String, PermissionJpaEntity> stored = permissionJpaRepository.findAll().stream()
                .collect(Collectors.toMap(PermissionJpaEntity::getCode, Function.identity()));

            for (Permission permission : Permission.values()) {
                PermissionJpaEntity entity = stored.remove(permission.name());
                if (entity == null) {
                    entity = new PermissionJpaEntity();
                    entity.setCode(permission.name());
                }
                entity.setDescription(permission.getDescription());
                entity.setActive(true);
                permissionJpaRepository.save(entity);
            }

            stored.values().forEach(entity -> entity.setActive(false));
            permissionJpaRepository.saveAll(stored.values());
        });

        log.info("Permissions synchronized: {}", Arrays.toString(Permission.values()));
    }
}

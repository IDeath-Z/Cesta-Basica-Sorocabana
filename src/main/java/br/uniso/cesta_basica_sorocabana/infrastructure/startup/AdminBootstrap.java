package br.uniso.cesta_basica_sorocabana.infrastructure.startup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.application.usecases.user.BootstrapAdminUseCase;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Creates the initial admin from ADMIN_EMAIL / ADMIN_PASSWORD. The password is only used on the first run;
// after that the admin changes it through the API and this never overwrites it.
@Slf4j
@Component
@RequiredArgsConstructor
public final class AdminBootstrap {

    private final BootstrapAdminUseCase bootstrapAdminUseCase;
    private final TransactionalProxy transactionalProxy;

    @Value("${app.admin.role-name}")
    private String roleName;

    @Value("${app.admin.email}")
    private String email;

    @Value("${app.admin.password}")
    private String password;

    @Order(2)
    @EventListener(ApplicationReadyEvent.class)
    public void bootstrap() {
        if (email.isBlank() || password.isBlank()) {
            log.warn("ADMIN_EMAIL or ADMIN_PASSWORD not set, skipping initial admin creation");
            return;
        }

        var output = transactionalProxy.execute(() -> bootstrapAdminUseCase.execute(
            new BootstrapAdminUseCase.Input(roleName, email, password)
        ));

        if (output.userCreated())
            log.info("Initial admin {} created with role {}", email, roleName);
    }
}

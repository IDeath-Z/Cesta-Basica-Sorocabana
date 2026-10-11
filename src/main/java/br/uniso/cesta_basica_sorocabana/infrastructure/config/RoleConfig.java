package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.role.UpdateRoleStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.role.CreateRoleUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.role.FetchPermissionsUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.role.FetchRoleUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.role.FetchRolesUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.role.UpdateRoleUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;

@Configuration
public class RoleConfig {

    @Bean
    public CreateRoleUseCase createRoleUseCase(RoleRepository roleRepository, AuditRecorder auditRecorder) {
        return new CreateRoleUseCase(roleRepository, auditRecorder);
    }

    @Bean
    public UpdateRoleUseCase updateRoleUseCase(RoleRepository roleRepository, AuditRecorder auditRecorder, @Value("${app.admin.role-name}") String protectedRoleName) {
        return new UpdateRoleUseCase(roleRepository, auditRecorder, protectedRoleName);
    }

    @Bean
    public FetchRoleUseCase fetchRoleUseCase(RoleRepository roleRepository) {
        return new FetchRoleUseCase(roleRepository);
    }

    @Bean
    public FetchRolesUseCase fetchRolesUseCase(RoleRepository roleRepository) {
        return new FetchRolesUseCase(roleRepository);
    }

    @Bean
    public UpdateRoleStatusUseCase updateRoleStatusUseCase(RoleRepository roleRepository, AuditRecorder auditRecorder, @Value("${app.admin.role-name}") String protectedRoleName) {
        return new UpdateRoleStatusUseCase(roleRepository, auditRecorder, protectedRoleName);
    }

    @Bean
    public FetchPermissionsUseCase fetchPermissionsUseCase() {
        return new FetchPermissionsUseCase();
    }
}

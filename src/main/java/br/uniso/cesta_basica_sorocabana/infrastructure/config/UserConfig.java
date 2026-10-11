package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.BootstrapAdminUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.ChangePasswordUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.ChangeUserRoleUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.UpdateUserStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.CreateUserUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.FetchCurrentUserUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.FetchUserUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.FetchUsersUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.ResetUserPasswordUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.UserRoleValidator;
import br.uniso.cesta_basica_sorocabana.domain.ports.PasswordHasher;
import br.uniso.cesta_basica_sorocabana.domain.ports.RoleRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.UserRepository;

@Configuration
public class UserConfig {

    @Bean
    public UserRoleValidator userRoleValidator(RoleRepository roleRepository) {
        return new UserRoleValidator(roleRepository);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher, UserRoleValidator userRoleValidator, AuditRecorder auditRecorder) {
        return new CreateUserUseCase(userRepository, passwordHasher, userRoleValidator, auditRecorder);
    }

    @Bean
    public FetchUserUseCase fetchUserUseCase(UserRepository userRepository) {
        return new FetchUserUseCase(userRepository);
    }

    @Bean
    public FetchUsersUseCase fetchUsersUseCase(UserRepository userRepository) {
        return new FetchUsersUseCase(userRepository);
    }

    @Bean
    public FetchCurrentUserUseCase fetchCurrentUserUseCase(UserRepository userRepository, RoleRepository roleRepository) {
        return new FetchCurrentUserUseCase(userRepository, roleRepository);
    }

    @Bean
    public ChangeUserRoleUseCase changeUserRoleUseCase(UserRepository userRepository, UserRoleValidator userRoleValidator, AuditRecorder auditRecorder) {
        return new ChangeUserRoleUseCase(userRepository, userRoleValidator, auditRecorder);
    }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(UserRepository userRepository, PasswordHasher passwordHasher, AuditRecorder auditRecorder) {
        return new ChangePasswordUseCase(userRepository, passwordHasher, auditRecorder);
    }

    @Bean
    public ResetUserPasswordUseCase resetUserPasswordUseCase(UserRepository userRepository, PasswordHasher passwordHasher, AuditRecorder auditRecorder) {
        return new ResetUserPasswordUseCase(userRepository, passwordHasher, auditRecorder);
    }

    @Bean
    public UpdateUserStatusUseCase updateUserStatusUseCase(UserRepository userRepository, AuditRecorder auditRecorder) {
        return new UpdateUserStatusUseCase(userRepository, auditRecorder);
    }

    @Bean
    public BootstrapAdminUseCase bootstrapAdminUseCase(RoleRepository roleRepository, UserRepository userRepository, PasswordHasher passwordHasher) {
        return new BootstrapAdminUseCase(roleRepository, userRepository, passwordHasher);
    }
}

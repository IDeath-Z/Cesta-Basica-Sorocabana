package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.usecases.auth.ProcessUserLoginUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.TokenProvider;

@Configuration
public class AuthConfig {

    @Bean
    public ProcessUserLoginUseCase processUserLoginUseCase(TokenProvider tokenProvider) {
        return new ProcessUserLoginUseCase(tokenProvider);
    }
}

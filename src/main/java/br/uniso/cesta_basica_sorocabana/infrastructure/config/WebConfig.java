package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.uniso.cesta_basica_sorocabana.presentation.controllers.GlobalExceptionHandler;

// Prefixes every controller of the presentation layer with the API base path (e.g. /api/v1).
// Scoped to our package so Swagger's own endpoints stay at /v3/api-docs and /swagger-ui.
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.api.base-path}")
    private String basePath;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(basePath, HandlerTypePredicate.forBasePackageClass(GlobalExceptionHandler.class));
    }
}

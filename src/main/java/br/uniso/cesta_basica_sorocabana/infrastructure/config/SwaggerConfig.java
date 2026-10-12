package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import java.util.Comparator;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;

import br.uniso.cesta_basica_sorocabana.infrastructure.enums.AuthScheme;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.presentation.dto.exceptions.ApiErrorResponse;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    private static final String ERROR_SCHEMA_REF = "#/components/schemas/" + ApiErrorResponse.class.getSimpleName();

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes(AuthScheme.BEARER.getSecuritySchemeName(),
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(AuthScheme.BEARER.getScheme())
                        .bearerFormat(AuthScheme.BEARER.getTokenFormat())
                )
            ).addSecurityItem(new SecurityRequirement().addList(AuthScheme.BEARER.getSecuritySchemeName()));
    }

    // Controllers no longer reference ApiErrorResponse directly, so its schema is registered here.
    @Bean
    public OpenApiCustomizer errorSchemaCustomizer() {
        return openApi -> ModelConverters.getInstance().readAll(ApiErrorResponse.class)
            .forEach((name, schema) -> openApi.getComponents().addSchemas(name, schema));
    }

    // Adds the responses every route shares, so controllers only document their own (201, 404, 409...):
    // 401 on every authenticated route, 403 on routes with @RequiresPermission (naming the permission),
    // 500 everywhere, and ApiErrorResponse as the body of every error. Descriptions already written on a route are kept.
    @Bean
    public OperationCustomizer standardResponsesCustomizer() {
        return (operation, handlerMethod) -> {
            ApiResponses responses = operation.getResponses();

            if (!isPublic(handlerMethod))
                responses.putIfAbsent(code(HttpStatus.UNAUTHORIZED), new ApiResponse().description("Missing or invalid token"));

            RequiresPermission permission = findPermission(handlerMethod);
            if (permission != null)
                responses.putIfAbsent(code(HttpStatus.FORBIDDEN), new ApiResponse().description("User lacks permission " + permission.value()));

            responses.putIfAbsent(code(HttpStatus.INTERNAL_SERVER_ERROR), new ApiResponse().description("Unexpected error"));

            // springdoc fills undeclared content with the method's return type, so errors are always overwritten.
            responses.forEach((status, response) -> {
                if (Integer.parseInt(status) >= 400)
                    response.setContent(errorContent());
            });

            operation.setResponses(sortedByStatus(responses));
            return operation;
        };
    }

    // Public routes are the ones that opt out of the global bearer requirement with an empty @SecurityRequirements.
    private boolean isPublic(HandlerMethod handlerMethod) {
        SecurityRequirements requirements = handlerMethod.getMethodAnnotation(SecurityRequirements.class);
        return requirements != null && requirements.value().length == 0;
    }

    private RequiresPermission findPermission(HandlerMethod handlerMethod) {
        RequiresPermission onMethod = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), RequiresPermission.class);
        return onMethod != null
            ? onMethod
            : AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), RequiresPermission.class);
    }

    private Content errorContent() {
        return new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
            new io.swagger.v3.oas.models.media.MediaType().schema(new Schema<>().$ref(ERROR_SCHEMA_REF)));
    }

    private ApiResponses sortedByStatus(ApiResponses responses) {
        ApiResponses sorted = new ApiResponses();
        responses.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
            .forEach(entry -> sorted.addApiResponse(entry.getKey(), entry.getValue()));
        return sorted;
    }

    private String code(HttpStatus status) {
        return String.valueOf(status.value());
    }
}

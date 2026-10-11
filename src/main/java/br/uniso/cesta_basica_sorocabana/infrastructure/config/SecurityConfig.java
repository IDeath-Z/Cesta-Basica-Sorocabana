package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.annotation.AnnotationTemplateExpressionDefaults;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import br.uniso.cesta_basica_sorocabana.application.exceptions.UnauthorizedException;
import br.uniso.cesta_basica_sorocabana.infrastructure.enums.PublicRoute;
import br.uniso.cesta_basica_sorocabana.infrastructure.enums.errors.AuthError;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(CorsProperties.class)
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final HandlerExceptionResolver exceptionResolver;
    private final CorsProperties corsProperties;
    private final String basePath;

    public SecurityConfig(SecurityFilter securityFilter, @Qualifier("handlerExceptionResolver") 
                            HandlerExceptionResolver exceptionResolver, CorsProperties corsProperties,
                            @Value("${app.api.base-path}") String basePath) {
        this.securityFilter     = securityFilter;
        this.exceptionResolver  = exceptionResolver;
        this.corsProperties     = corsProperties;
        this.basePath           = basePath;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Lets @RequiresPermission fill {value} in its @PreAuthorize expression.
    @Bean
    public static AnnotationTemplateExpressionDefaults templateExpressionDefaults() {
        return new AnnotationTemplateExpressionDefaults();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(publicRoutes()).permitAll()
                .anyRequest().authenticated())
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) ->
                    exceptionResolver.resolveException(request, response, null,
                        new UnauthorizedException(AuthError.ACCESS_DENIED.message()))))
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    private RequestMatcher[] publicRoutes() {
        return Arrays.stream(PublicRoute.values())
            .map(route -> {
                String path = route.isVersioned() ? basePath + route.getPath() : route.getPath();
                return route.getMethod() == null
                    ? PathPatternRequestMatcher.pathPattern(path)
                    : PathPatternRequestMatcher.pathPattern(HttpMethod.valueOf(route.getMethod()), path);
            })
            .toArray(RequestMatcher[]::new);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(corsProperties.allowedOriginPatterns());
        config.setAllowedMethods(Stream.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE)
            .map(HttpMethod::name)
            .toList());
        config.setAllowedHeaders(List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
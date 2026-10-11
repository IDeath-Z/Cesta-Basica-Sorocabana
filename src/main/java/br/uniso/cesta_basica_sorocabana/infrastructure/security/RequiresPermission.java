package br.uniso.cesta_basica_sorocabana.infrastructure.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;

// Typed shortcut for @PreAuthorize("hasAuthority('X')"): the route only runs if the user's role grants the permission.
// {value} is resolved by the AnnotationTemplateExpressionDefaults bean in SecurityConfig.
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('{value}')")
public @interface RequiresPermission {

    Permission value();
}

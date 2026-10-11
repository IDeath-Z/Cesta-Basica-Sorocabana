package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.permission;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "permission")
public class PermissionJpaEntity {

    @Id
    @Column(name = "code", nullable = false, updatable = false, columnDefinition = "TEXT")
    private String code;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active;
}

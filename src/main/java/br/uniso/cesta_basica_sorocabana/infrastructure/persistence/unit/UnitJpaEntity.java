package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.unit;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "unit")
public class UnitJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "quantity", nullable = false, precision = 12, scale = 4)
    private BigDecimal quantity;

    @Column(name = "measure", nullable = false, columnDefinition = "TEXT")
    private String measure;

    @Column(name = "active", nullable = false)
    private boolean active;
}

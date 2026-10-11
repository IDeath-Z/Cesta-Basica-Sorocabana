package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.brand.BrandJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.product.ProductJpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product_brand")
public class ProductBrandJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_brand_product"))
    private ProductJpaEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_brand_brand"))
    private BrandJpaEntity brand;

    @Column(name = "active", nullable = false)
    private boolean active;
}

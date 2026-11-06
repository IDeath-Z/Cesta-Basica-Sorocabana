package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrandstore;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand.ProductBrandJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.store.StoreJpaEntity;

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
@Table(name = "product_brand_store")
public class ProductBrandStoreJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_brand_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pbs_product_brand"))
    private ProductBrandJpaEntity productBrand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pbs_store"))
    private StoreJpaEntity store;

    @Column(name = "search_name", nullable = false, columnDefinition = "TEXT")
    private String searchName;

    @Column(name = "active", nullable = false)
    private boolean active;
}

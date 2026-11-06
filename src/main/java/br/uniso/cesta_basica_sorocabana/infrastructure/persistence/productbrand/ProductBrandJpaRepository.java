package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.uniso.cesta_basica_sorocabana.domain.shared.ProductBrandSummary;

public interface ProductBrandJpaRepository extends JpaRepository<ProductBrandJpaEntity, UUID> {

    List<ProductBrandJpaEntity> findByProductId(UUID productId);

    @Query(value = """
        select new br.uniso.cesta_basica_sorocabana.domain.shared.ProductBrandSummary(
            productBrand.id, product.id, product.name, product.specification,
            unit.quantity, unit.measure, brand.id, brand.name, productBrand.active)
        from ProductBrandJpaEntity productBrand
        join productBrand.product product
        left join product.unit unit
        join productBrand.brand brand
        order by product.name, product.specification, unit.quantity, brand.name
        """, countQuery = "select count(productBrand) from ProductBrandJpaEntity productBrand")
    Page<ProductBrandSummary> findAllSummaries(Pageable pageable);

    boolean existsByProductIdAndBrandId(UUID productId, UUID brandId);
}

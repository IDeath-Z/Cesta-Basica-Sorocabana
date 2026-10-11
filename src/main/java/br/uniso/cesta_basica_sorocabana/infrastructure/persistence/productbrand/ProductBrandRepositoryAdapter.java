package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrand;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandRepository;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.brand.BrandJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.brand.BrandJpaRepository;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.product.ProductJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.product.ProductJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class ProductBrandRepositoryAdapter implements ProductBrandRepository {

    private final ProductBrandJpaRepository productBrandJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final BrandJpaRepository brandJpaRepository;
    private final ProductBrandMapper productBrandMapper;

    @Override
    public ProductBrand save(ProductBrand productBrand) {
        ProductJpaEntity productProxy = productJpaRepository.getReferenceById(productBrand.getProductId());
        BrandJpaEntity brandProxy = brandJpaRepository.getReferenceById(productBrand.getBrandId());

        ProductBrandJpaEntity entity = productBrandMapper.toJpa(productBrand, productProxy, brandProxy);
        return productBrandMapper.toDomain(productBrandJpaRepository.save(entity));
    }

    @Override
    public Optional<ProductBrand> findById(UUID id) {
        return productBrandJpaRepository.findById(id).map(productBrandMapper::toDomain);
    }

    @Override
    public List<ProductBrand> findByProductId(UUID productId) {
        return productBrandJpaRepository.findByProductId(productId)
            .stream()
            .map(productBrandMapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsByProductIdAndBrandId(UUID productId, UUID brandId) {
        return productBrandJpaRepository.existsByProductIdAndBrandId(productId, brandId);
    }
}

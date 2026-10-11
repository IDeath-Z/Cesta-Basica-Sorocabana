package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.product;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.Product;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.category.CategoryJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.category.CategoryJpaRepository;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.PageConverter;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.unit.UnitJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.unit.UnitJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final UnitJpaRepository unitJpaRepository;
    private final ProductMapper productMapper;

    @Override
    public Product save(Product product) {
        CategoryJpaEntity categoryProxy = categoryJpaRepository.getReferenceById(product.getCategoryId());
        UnitJpaEntity unitProxy = product.getUnitId() != null ? unitJpaRepository.getReferenceById(product.getUnitId()) : null;

        ProductJpaEntity entity = productMapper.toJpa(product, categoryProxy, unitProxy);
        return productMapper.toDomain(productJpaRepository.save(entity));
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return productJpaRepository.findById(id).map(productMapper::toDomain);
    }

    @Override
    public DomainPageResult<Product> findAll(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<ProductJpaEntity> page = productJpaRepository.findAll(springPage);

        return PageConverter.toDomain(page, productMapper::toDomain);
    }

    @Override
    public DomainPageResult<Product> findByCategoryId(UUID categoryId, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<ProductJpaEntity> page = productJpaRepository.findByCategoryId(categoryId, springPage);

        return PageConverter.toDomain(page, productMapper::toDomain);
    }

    // The unique constraint uses NULLS NOT DISTINCT, so a null specification must be compared with IS NULL.
    @Override
    public boolean existsByNameAndSpecification(String name, String specification) {
        return specification == null
            ? productJpaRepository.existsByNameAndSpecificationIsNull(name)
            : productJpaRepository.existsByNameAndSpecification(name, specification);
    }
}

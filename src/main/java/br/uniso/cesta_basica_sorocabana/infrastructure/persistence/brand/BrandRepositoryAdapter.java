package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.brand;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.Brand;
import br.uniso.cesta_basica_sorocabana.domain.ports.BrandRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.PageConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class BrandRepositoryAdapter implements BrandRepository {

    private final BrandJpaRepository brandJpaRepository;
    private final BrandMapper brandMapper;

    @Override
    public Brand save(Brand brand) {
        BrandJpaEntity entity = brandMapper.toJpa(brand);
        return brandMapper.toDomain(brandJpaRepository.save(entity));
    }

    @Override
    public Optional<Brand> findById(UUID id) {
        return brandJpaRepository.findById(id).map(brandMapper::toDomain);
    }

    @Override
    public DomainPageResult<Brand> findAll(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<BrandJpaEntity> page = brandJpaRepository.findAll(springPage);

        return PageConverter.toDomain(page, brandMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return brandJpaRepository.existsByName(name);
    }
}

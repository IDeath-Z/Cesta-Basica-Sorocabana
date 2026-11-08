package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.basket;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import br.uniso.cesta_basica_sorocabana.domain.shared.BasketItem;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrandstore.ProductBrandStoreJpaEntity;

public interface BasketJpaRepository extends Repository<ProductBrandStoreJpaEntity, UUID> {

    @Query("""
        select new br.uniso.cesta_basica_sorocabana.domain.shared.BasketItem(
            link.id, productBrand.id, link.searchName,
            product.id, product.name, product.specification, product.notes,
            category.id, category.name,
            unit.id, unit.quantity, unit.measure,
            brand.id, brand.name,
            store.id, store.name, store.storeUrl)
        from ProductBrandStoreJpaEntity link
        join link.productBrand productBrand
        join productBrand.product product
        join product.category category
        left join product.unit unit
        join productBrand.brand brand
        join link.store store
        where store.id = :storeId and link.searchName is not null
          and link.active = true and productBrand.active = true and product.active = true
          and brand.active = true and store.active = true
        order by product.name, product.specification, brand.name
        """)
    List<BasketItem> findActiveByStoreId(@Param("storeId") UUID storeId);

    @Query("""
        select new br.uniso.cesta_basica_sorocabana.domain.shared.BasketItem(
            link.id, productBrand.id, link.searchName,
            product.id, product.name, product.specification, product.notes,
            category.id, category.name,
            unit.id, unit.quantity, unit.measure,
            brand.id, brand.name,
            store.id, store.name, store.storeUrl,
            price.price)
        from SurveyItemJpaEntity surveyItem
        join surveyItem.productBrand productBrand
        join productBrand.product product
        join product.category category
        left join product.unit unit
        join productBrand.brand brand
        join SurveyItemPriceJpaEntity price on price.id.surveyItemId = surveyItem.id
        join StoreJpaEntity store on store.id = price.id.storeId
        left join ProductBrandStoreJpaEntity link on link.productBrand = productBrand and link.store = store
        where surveyItem.survey.id = :surveyId
        order by store.name, product.name, product.specification, brand.name
        """)
    List<BasketItem> findSavedBySurveyId(@Param("surveyId") UUID surveyId);
}

package org.madi.productaggregator.web.dao;

import org.madi.productaggregator.web.entities.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update  product p" +
            "    set p.aggr_product_id = (select id from aggr_products ap where ap.name = p.name and ap.unit = p.unit limit 1)" +
            "    where  market_id = ?1")
    void linkProductWithAggrProduct( Long marketId);

    ProductEntity findProductEntityByMarketEntityIdAndExternalId(Long marketId, String externalId);
    List<ProductEntity> findProductEntitiesByAggregatorProductEntityIdIsNull(Pageable pageable);
    List<ProductEntity> findProductEntitiesByAggregatorProductEntityIdIsNullAndNameLike(String productName, Pageable pageable);
    int countByAggregatorProductEntityIdIsNull();
    int countByAggregatorProductEntityIdIsNullAndNameLike(String productName);
}

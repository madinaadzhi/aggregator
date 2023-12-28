package org.madi.productaggregator.web.dao;

import org.madi.productaggregator.web.entities.AggregatorProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AggregatorProductRepository extends JpaRepository<AggregatorProductEntity, Long> {
    List<AggregatorProductEntity> findAggregatorProductEntitiesByCategoryId(Long categoryId);
    List<AggregatorProductEntity> findAggregatorProductEntitiesByCategoryIdInAndNameLike(List<Long> categoryIds, String productName, Pageable pageable);
    List<AggregatorProductEntity> findAggregatorProductEntitiesByNameLike(String productName, Pageable pageable);
    List<AggregatorProductEntity> findAggregatorProductEntitiesByCategoryIdIn(List<Long> categoryIds, Pageable pageable);
    List<AggregatorProductEntity> findAllBy(Pageable pageable);
    int countByCategoryIdIn(List<Long> categoryIds);
    int countBy();
    int countByCategoryIdInAndNameLike(List<Long> categoryIds, String productName);
    int countByNameLike(String productName);
    List<AggregatorProductEntity> findFirst10ByNameLike(String productNameTemplate);
}

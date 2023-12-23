package org.madi.productaggregator.web.dao;

import org.madi.productaggregator.web.entities.AggregatorProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AggregatorProductRepository extends PagingAndSortingRepository<AggregatorProductEntity, Long> {
    List<AggregatorProductEntity> findAggregatorProductEntitiesByCategoryId(Long categoryId);
    List<AggregatorProductEntity> findAggregatorProductEntitiesByCategoryIdInAndNameLike(List<Long> categoryIds, String productName, Pageable pageable);
    List<AggregatorProductEntity> findAggregatorProductEntitiesByCategoryIdIn(List<Long> categoryIds, Pageable pageable);
    int countByCategoryIdIn(List<Long> categoryIds);
    int countByCategoryIdInAndNameLike(List<Long> categoryIds, String productName);
}

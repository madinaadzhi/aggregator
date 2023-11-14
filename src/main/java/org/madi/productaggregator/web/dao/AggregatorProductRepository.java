package org.madi.productaggregator.web.dao;

import org.madi.productaggregator.web.entities.AggregatorProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AggregatorProductRepository extends CrudRepository<AggregatorProductEntity, Long>{
    List<AggregatorProductEntity> findAggregatorProductEntitiesByCategoryId(Long categoryId);
    List<AggregatorProductEntity> findAggregatorProductEntitiesByCategoryId(Long categoryId, Pageable pageable);

}

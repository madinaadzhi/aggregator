package org.madi.productaggregator.web.dao;

import org.madi.productaggregator.web.entities.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
    List<CategoryEntity> getCategoryEntitiesByMarketId(Long marketId);
}

package org.madi.productaggregator.web.dao;

import org.madi.productaggregator.web.entities.MarketEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketRepository extends ListCrudRepository<MarketEntity, Long> {
    MarketEntity findMarketEntitiesByDomainName(String domainName);
    List<MarketEntity> findByNameNotIgnoreCase(String name);
}

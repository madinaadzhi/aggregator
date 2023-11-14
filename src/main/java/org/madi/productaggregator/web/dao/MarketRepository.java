package org.madi.productaggregator.web.dao;

import org.madi.productaggregator.web.entities.MarketEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends CrudRepository<MarketEntity, Long> {
    MarketEntity findMarketEntitiesByDomainName(String domainName);

}

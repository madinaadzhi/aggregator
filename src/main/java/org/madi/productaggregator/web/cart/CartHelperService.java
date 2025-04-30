package org.madi.productaggregator.web.cart;

import org.madi.productaggregator.web.dao.MarketRepository;
import org.madi.productaggregator.web.entities.MarketEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartHelperService {
    @Autowired
    private MarketRepository marketRepository;

    public List<MarketEntity> getMarkets() {
        return marketRepository.findByNameNotIgnoreCase("Aggregator");
    }
}

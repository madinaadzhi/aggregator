package org.madi.productaggregator.web.controllers;

import org.madi.productaggregator.web.dao.MarketRepository;
import org.madi.productaggregator.web.entities.MarketEntity;
import org.madi.productaggregator.web.market.api.Market;
import org.madi.productaggregator.web.market.api.MarketApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartHelper {
    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    @Qualifier("ForaMarketApi")
    private MarketApi foraMarketApi;

    @Autowired
    @Qualifier("SilpoMarketApi")
    private MarketApi silpoMarketApi;

    public List<Market> getMarkets() {
        Iterable<MarketEntity> marketEntities = marketRepository.findAll();
        List<Market> markets = new ArrayList<>();
        for (MarketEntity entity : marketEntities) {
            if (entity.getDomainName().equals(foraMarketApi.getMarketInfo().getDomainName())) {
                Market fora = foraMarketApi.getMarketInfo();
                markets.add(fora);
            } else if (entity.getDomainName().equals(silpoMarketApi.getMarketInfo().getDomainName())) {
                Market silpo = silpoMarketApi.getMarketInfo();
                markets.add(silpo);
            }
        }
        return markets;
    }
}

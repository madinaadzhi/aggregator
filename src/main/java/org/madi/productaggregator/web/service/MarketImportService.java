package org.madi.productaggregator.web.service;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.madi.productaggregator.web.dao.CategoryRepository;
import org.madi.productaggregator.web.dao.MarketRepository;
import org.madi.productaggregator.web.entities.CategoryEntity;
import org.madi.productaggregator.web.entities.MarketEntity;
import org.madi.productaggregator.web.market.api.Category;
import org.madi.productaggregator.web.market.api.Market;
import org.madi.productaggregator.web.market.api.MarketApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MarketImportService {
    @Autowired
    private MarketRepository marketRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public void importMarket(MarketApi marketApi) {
        String market = marketApi.getMarketInfo().getDomainName();
        log.info("[{}] Start importing ", market);
        log.info("[{}] Saving market to DB ", market);
        saveMarket(marketApi);
        log.info("[{}] Saving categories to DB ", market);
        saveCategories(marketApi);
        log.info("[{}] Done ", market);
//        List<Product> products = marketApi.getProducts();
    }

    private void saveMarket(MarketApi marketApi) {
        Market marketInfo = marketApi.getMarketInfo();
        MarketEntity marketEntity = new MarketEntity();
        marketEntity.setName(marketInfo.getName());
        marketEntity.setLogoUrl(marketInfo.getLogoUrl());
        marketEntity.setDomainName(marketInfo.getDomainName());
        marketRepository.save(marketEntity);
    }

    private void saveCategories(MarketApi marketApi) {
        Market marketInfo = marketApi.getMarketInfo();
        Long marketId = marketRepository.findMarketEntitiesByDomainName(marketInfo.getDomainName()).getId();

        List<Category> categories = marketApi.getCategories();
        Map<String, Long> externalToInternalId = new HashMap<>();
        for (Category category : categories) {
            String externalId = category.getId();
            String name = category.getName();
            Long parentId = externalToInternalId.get(category.getParentId());
            String logoUrl = category.getLogoUrl();

            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setName(name);
            categoryEntity.setParentId(parentId);
            categoryEntity.setMarketId(marketId);
            categoryEntity.setExternalId(externalId);
            categoryEntity.setLogoUrl(logoUrl);

            CategoryEntity saved = categoryRepository.save(categoryEntity);
            externalToInternalId.put(externalId, saved.getId());
        }
    }
}

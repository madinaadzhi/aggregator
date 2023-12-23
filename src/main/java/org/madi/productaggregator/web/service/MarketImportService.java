package org.madi.productaggregator.web.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.madi.productaggregator.web.dao.CategoryRepository;
import org.madi.productaggregator.web.dao.MarketRepository;
import org.madi.productaggregator.web.dao.ProductRepository;
import org.madi.productaggregator.web.entities.CategoryEntity;
import org.madi.productaggregator.web.entities.MarketEntity;
import org.madi.productaggregator.web.entities.ProductEntity;
import org.madi.productaggregator.web.market.api.Category;
import org.madi.productaggregator.web.market.api.Market;
import org.madi.productaggregator.web.market.api.MarketApi;
import org.madi.productaggregator.web.market.api.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private ProductRepository productRepository;

    public void importMarket(MarketApi marketApi) {
        String domainName = marketApi.getMarketInfo().getDomainName();
        log.info("[{}] Start importing ", domainName);
        MarketEntity market = marketRepository.findMarketEntitiesByDomainName(domainName);
        if (market == null) {
            log.info("[{}] Saving market to DB ", domainName);
            saveMarket(marketApi);
            market = marketRepository.findMarketEntitiesByDomainName(domainName);
        } else {
            log.info("Market already has been imported");
        }
        int categoriesCount = categoryRepository.countByMarketId(market.getId());
        if (categoriesCount == 0) {
            log.info("[{}] Saving categories to DB ", domainName);

            saveCategories(marketApi);
            log.info("[{}] Done ", domainName);
            log.info("[{}] Saving products to DB ", domainName);
        } else {
            log.info("Categories already have been imported");
        }

        while (true) {
            try {
                saveProducts(marketApi, true);
                break;
            } catch (Exception e) {
                log.warn("Error occurred during import process. Waiting 1 minute to retry. Details: {}", e.getMessage());
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException ex) {
                }
            }
        }
        log.info("[{}] Done ", domainName);
        log.info("[{}] Add products to aggregator", domainName);
        productRepository.linkProductWithAggrProduct(market.getId());
        log.info("[{}] Done ", domainName);
    }

    public void updatePrice(MarketApi marketApi) {
        String domainName = marketApi.getMarketInfo().getDomainName();
        log.info("[{}] Updating price ", domainName);

        while (true) {
            try {
                saveProducts(marketApi, false);
                break;
            } catch (Exception e) {
                log.warn("Error occurred during price update. Waiting 1 minute to retry. Details: {}", e.getMessage(), e);
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException ex) {
                }
            }
        }
        log.info("[{}] Done ", domainName);
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

    private void saveProducts(MarketApi marketApi, boolean isImport) {
        Market marketInfo = marketApi.getMarketInfo();
        MarketEntity marketEntity = marketRepository.findMarketEntitiesByDomainName(marketInfo.getDomainName());

        List<CategoryEntity> categoriesForProcessing = getCategories(marketEntity, isImport);
        log.info("[{}] Found {} categories ", marketInfo.getName(), categoriesForProcessing.size());
        for (int i = 0; i < categoriesForProcessing.size(); i++) {
            CategoryEntity notProcessedCategory = categoriesForProcessing.get(i);
            log.info("[{}] {} of {} category is being processed. Category Id: {} ",
                    marketInfo.getName(), i + 1, categoriesForProcessing.size(), notProcessedCategory.getId());
            List<Product> products = marketApi.getProducts(notProcessedCategory.getExternalId());
            for (Product product : products) {
                String externalId = product.getId();
                String name = product.getName();
                double price = product.getPrice();
                Boolean isAvailable = product.isAvailable();
                String externalCategoryId = product.getCategoryId();
                String siteUrl = product.getSiteUrl();
                String imageUrl = product.getImageUrl();
                String unit = product.getUnit();

                if (!StringUtils.isNumeric(externalCategoryId)) {
                    log.warn("{} is not a valid category id. ignoring product {}", externalCategoryId, product.getName());
                    continue;
                }

                if (isImport) {
                    Long categoryId = categoryRepository.getCategoryEntityByExternalIdAndMarketId(externalCategoryId, marketEntity.getId()).getId();
                    ProductEntity productEntity = new ProductEntity();
                    productEntity.setExternalId(externalId);
                    productEntity.setName(name);
                    productEntity.setPrice(price);
                    productEntity.setAvailable(isAvailable);
                    productEntity.setCategoryId(categoryId);
                    productEntity.setSiteUrl(siteUrl);
                    productEntity.setImgUrl(imageUrl);
                    productEntity.setMarketEntity(marketEntity);
                    productEntity.setUnit(unit);
                    try {
                        productRepository.save(productEntity);
                    } catch (Exception e) {
                        log.info("[{}] could not persist product {} ",
                                marketInfo.getName(), productEntity, e);
                    }
                } else {
                    try {
                        ProductEntity persistedProduct = productRepository.findProductEntityByMarketEntityIdAndExternalId(marketEntity.getId(), externalId);
                        if (persistedProduct != null) {
                            if (!persistedProduct.getPrice().equals(price) || persistedProduct.isAvailable() == isAvailable) {
                                log.info("[{}] updating product {}. Changed price from {} to {}. Changed availability from {} to {}",
                                        marketInfo.getName(), externalId, persistedProduct.getPrice(), price, persistedProduct.isAvailable(), isAvailable);
                                persistedProduct.setPrice(price);
                                persistedProduct.setAvailable(isAvailable);
                                productRepository.save(persistedProduct);
                            }
                        } else {
                            log.info("[{}] there is no product {} in the DB",
                                    marketInfo.getName(), externalId);
                        }
                    } catch (Exception e) {
                        log.info("[{}] could not update product {} ",
                                marketInfo.getName(), externalId, e);
                    }
                }
            }
            notProcessedCategory.setImported(true);
            categoryRepository.save(notProcessedCategory);
        }
    }

    private List<CategoryEntity> getCategories(MarketEntity marketEntity, boolean isImport) {
        if (isImport) {
            return getNotImportedCategories(marketEntity.getId());
        } else {
            return categoryRepository.getCategoryEntitiesByMarketId(marketEntity.getId());
        }
    }

    private List<CategoryEntity> getNotImportedCategories(Long marketId) {
        List<CategoryEntity> notImportedCategories = categoryRepository.getCategoryEntitiesByMarketIdAndIsImportedFalse(marketId);
        List<CategoryEntity> result = new ArrayList<>();
        addToListWithNotImportedCategories(notImportedCategories, result);
        return result;
    }

    private void addToListWithNotImportedCategories(List<CategoryEntity> notImportedCategories, List<CategoryEntity> result) {
        for (CategoryEntity notImportedCategory : notImportedCategories) {
            if (notImportedCategories.size() == 0) {
                addToListWithNotImportedCategories(notImportedCategories, result);
            } else if (notImportedCategory.getChildCategories().size() == 0) {
                result.add(notImportedCategory);
            }
        }
    }
}


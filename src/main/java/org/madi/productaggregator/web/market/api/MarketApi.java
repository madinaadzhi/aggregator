package org.madi.productaggregator.web.market.api;

import java.util.List;

public interface MarketApi {
    Market getMarketInfo();
    List<Category> getCategories();
    List<Product> getProducts(String categoryId);
}

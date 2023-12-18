package org.madi.productaggregator.web.market.impl.fora;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.madi.productaggregator.web.market.api.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ForaMarketApi implements MarketApi {
    private static final String URL = "https://api.catalog.ecom.fora.ua/api/2.0/exec/EcomCatalogGlobal";

    @Override
    public Market getMarketInfo() {
        return new Market() {
            @Override
            public String getDomainName() {
                return "shop.fora.ua";
            }

            @Override
            public String getName() {
                return "Fora";
            }

            @Override
            public String getLogoUrl() {
                return "https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/%D0%9B%D0%BE%D0%B3%D0%BE%D1%82%D0%B8%D0%BF_%D0%BC%D0%B5%D1%80%D0%B5%D0%B6%D1%96_%D1%81%D1%83%D0%BF%D0%B5%D1%80%D0%BC%D0%B0%D1%80%D0%BA%D0%B5%D1%82%D1%96%D0%B2_%D0%A4%D0%BE%D1%80%D0%B0.svg/1200px-%D0%9B%D0%BE%D0%B3%D0%BE%D1%82%D0%B8%D0%BF_%D0%BC%D0%B5%D1%80%D0%B5%D0%B6%D1%96_%D1%81%D1%83%D0%BF%D0%B5%D1%80%D0%BC%D0%B0%D1%80%D0%BA%D0%B5%D1%82%D1%96%D0%B2_%D0%A4%D0%BE%D1%80%D0%B0.svg.png";
            }

            @Override
            public String getSiteUrl() {
                return "https://shop.fora.ua/";
            }
        };
    }

    @Override
    public List<Category> getCategories() {
        try {
            String foraCategoriesJson = getForaCategoriesJson();
            return parseCategories(foraCategoriesJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getForaCategoriesJson() throws IOException {
        return Jsoup.connect(URL)
                .header("content-type", "application/json")
                .requestBody("{\"method\":\"GetCategories\",\"data\":{\"deliveryType\":2,\"filialId\":310,\"merchantId\":2}}")
                .method(org.jsoup.Connection.Method.POST)
                .ignoreContentType(true)
                .execute().body();
    }

    private List<Category> parseCategories(String categoriesJson) throws JSONException {
        JSONObject jsonObject = new JSONObject(categoriesJson);
        JSONArray items = jsonObject.getJSONArray("tree");
        List<Category> foraCategories = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            String externalId = String.valueOf(items.getJSONObject(i).get("id"));
            String name = items.getJSONObject(i).getString("name");
            String parentId = String.valueOf(items.getJSONObject(i).get("parentId"));
            String logoUrl = String.valueOf(items.getJSONObject(i).get("mobileAppIconPath"));
            if (parentId.equals("null")) {
                parentId = null;
            }
            foraCategories.add(new ForaCategory(externalId, name, parentId, logoUrl));
        }
        return foraCategories;
    }

    @Override
    public List<Product> getProducts(String categoryId) {
        log.info("[{}] Start importing products ", getMarketInfo().getName());
        try {
            List<Product> products = new ArrayList<>();
            int iteration = 1;
            int expectedProductCnt = 0;
            int fetchedProductCnt = 0;
            while (true) {
                int from = (iteration - 1) * 30 + 1;
                int to = iteration * 30;
                log.info("[{}] Making request: categoryId - {}, from - {}, to - {} ", getMarketInfo().getName(), categoryId, from, to);
                String json = ForaUtils.getProductPageByCategoryId(categoryId, from, to);
                List<Product> productList = ForaUtils.getProduct(json);
                fetchedProductCnt += productList.size();
                products.addAll(productList);
                if (iteration == 1) {
                    JSONObject jsonObject = new JSONObject(json);
                    expectedProductCnt = jsonObject.getInt("itemsCount");
                    log.info("[{}] Category {} contains {} products ", getMarketInfo().getName(), categoryId, expectedProductCnt);
                }
                if (fetchedProductCnt == expectedProductCnt) {
                    break;
                }
                iteration++;
            }
            return products;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stock getStock(String productId) {
        return null;
    }
}

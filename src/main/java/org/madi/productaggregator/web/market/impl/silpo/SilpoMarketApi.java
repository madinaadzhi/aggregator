package org.madi.productaggregator.web.market.impl.silpo;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.madi.productaggregator.web.dao.CategoryRepository;
import org.madi.productaggregator.web.market.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.madi.productaggregator.web.market.impl.silpo.SilpoUtils.getProductPageByCategoryId;

@Service("SilpoMarketApi")
@Slf4j
public class SilpoMarketApi implements MarketApi {
    private static final String URL = "https://api.catalog.ecom.silpo.ua/api/2.0/exec/EcomCatalogGlobal";

    @Override
    public Market getMarketInfo() {
        return new Market() {
            @Override
            public String getDomainName() {
                return "shop.silpo.ua";
            }

            @Override
            public String getName() {
                return "Silpo";
            }

            @Override
            public String getLogoUrl() {
                return "https://silpo.ua/pcs/logo.svg?id=Wed%20Nov%2001%202023%2014:47:48%20GMT+0200%20(%D0%B7%D0%B0%20%D1%81%D1%85%D1%96%D0%B4%D0%BD%D0%BE%D1%94%D0%B2%D1%80%D0%BE%D0%BF%D0%B5%D0%B9%D1%81%D1%8C%D0%BA%D0%B8%D0%BC%20%D1%81%D1%82%D0%B0%D0%BD%D0%B4%D0%B0%D1%80%D1%82%D0%BD%D0%B8%D0%BC%20%D1%87%D0%B0%D1%81%D0%BE%D0%BC)";
            }

            @Override
            public String getSiteUrl() {
                return "https://shop.silpo.ua/";
            }
        };
    }


    @Override
    public List<Category> getCategories() {
        try {
            String silpoCategoriesJson = getSilpoCategoriesJson();
            return parseCategories(silpoCategoriesJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSilpoCategoriesJson() throws IOException {
        return Jsoup.connect(URL)
                .header("content-type", "application/json")
                .requestBody("{\"method\":\"GetCategories\",\"data\":{\"merchantId\":1,\"basketGuid\":\"\",\"deliveryType\":2,\"filialId\":3288}}")
                .method(org.jsoup.Connection.Method.POST)
                .ignoreContentType(true)
                .execute().body();
    }

    private List<Category> parseCategories(String categoriesJson) throws JSONException {
        JSONObject jsonObject = new JSONObject(categoriesJson);
        JSONArray items = jsonObject.getJSONArray("tree");
        List<Category> silpoCategories = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            String externalId = String.valueOf(items.getJSONObject(i).get("id"));
            String name = items.getJSONObject(i).getString("name");
            String parentId = String.valueOf(items.getJSONObject(i).get("parentId"));
            String logoUrl = String.valueOf(items.getJSONObject(i).get("mobileAppIconPath"));
            if (parentId.equals("null")) {
                parentId = null;
            }
            silpoCategories.add(new SilpoCategory(externalId, name, parentId, logoUrl));
        }
        return silpoCategories;
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
                String json = getProductPageByCategoryId(categoryId, from, to);
                List<Product> productList = SilpoUtils.getProduct(json);
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
}

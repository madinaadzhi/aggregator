package org.madi.productaggregator.web.market.impl.fora;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.madi.productaggregator.web.market.api.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Slf4j
public class ForaUtils {
    public static List<Product> getProduct(String json) {
        List<Product> products = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray items = jsonObject.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            JSONArray categories = items.getJSONObject(i).getJSONArray("categories");
            String externalId = String.valueOf(items.getJSONObject(i).get("id"));
            String name = items.getJSONObject(i).getString("name");
            double price = items.getJSONObject(i).getDouble("price");
            int quantity = items.getJSONObject(i).getInt("quantity");
            String categoryId = String.valueOf(categories.getJSONObject(categories.length() - 1).getInt("id"));
            String siteUrl = "https://shop.fora.ua/product/" + externalId;
            String imageUrl = items.getJSONObject(i).getString("mainImage");
            ForaProduct product = new ForaProduct(externalId, name, price, quantity > 0, categoryId, siteUrl, imageUrl);
            products.add(product);
        }
        return products;
    }

    public static String getProductPageByCategoryId(String categoryId, int from, int to) throws IOException {
        return Jsoup.connect("https://api.catalog.ecom.fora.ua/api/2.0/exec/EcomCatalogGlobal")
                .header("content-type", "application/json")
                .requestBody("{\"method\":\"GetSimpleCatalogItems\",\"data\":{\"merchantId\":2,\"deliveryType\":2,\"filialId\":310,\"From\":" + from + ",\"businessId\":1,\"To\":" + to + ",\"categoryId\":" + categoryId + ",\"RangeFilters\":{},\"MultiFilters\":{},\"UniversalFilters\":[],\"CategoryFilter\":[],\"Promos\":[]}}")
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .execute().body();
    }
}

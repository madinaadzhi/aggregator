package org.madi.productaggregator.web.market.impl.fora;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.madi.productaggregator.web.market.api.Product;

@AllArgsConstructor
@Data
public class ForaProduct implements Product {
    private String id;
    private String name;
    private Double price;
    private Boolean isAvailable;
    private String categoryId;
    private String siteUrl;
    private String imageUrl;
    private String unit;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public Boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public String getSiteUrl() {
        return siteUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String getUnit() {
        return unit;
    }
}

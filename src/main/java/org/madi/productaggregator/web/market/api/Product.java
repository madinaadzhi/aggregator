package org.madi.productaggregator.web.market.api;

public interface Product {
    String getId();
    String getName();
    Double getPrice();
    Boolean isAvailable();
    String getCategoryId();
    String getSiteUrl();
    String getImageUrl();
}

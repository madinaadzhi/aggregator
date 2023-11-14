package org.madi.productaggregator.web.market.api;

public interface Product {
    String getId();
    String getName();
    Double getPrice();
    Boolean isAvailable();
    String getSiteUrl();
    String getImageUrl();
}

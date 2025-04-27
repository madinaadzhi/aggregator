package org.madi.productaggregator.web.market.api;

public interface Market {
    String getDomainName();

    String getName();

    String getLogoUrl();

    String getAddress();

    String getSiteUrl();

    Double getLatitude();
    Double getLongitude();
}

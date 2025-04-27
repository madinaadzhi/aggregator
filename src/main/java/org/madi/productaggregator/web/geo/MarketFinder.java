package org.madi.productaggregator.web.geo;

import java.io.IOException;
import java.util.List;

public interface MarketFinder {
    List<MarketDist> findMarkets(double lat, double lng) throws IOException;
}

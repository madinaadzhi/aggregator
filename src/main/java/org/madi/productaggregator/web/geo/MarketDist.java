package org.madi.productaggregator.web.geo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.madi.productaggregator.web.market.api.Market;

@AllArgsConstructor
@Data
public class MarketDist {
    private Market market;
    private int distance;
}

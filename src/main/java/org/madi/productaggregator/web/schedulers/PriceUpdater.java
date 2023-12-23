package org.madi.productaggregator.web.schedulers;

import org.madi.productaggregator.web.market.api.MarketApi;
import org.madi.productaggregator.web.service.MarketImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PriceUpdater {
    public static final int SIX_HOURS = 6 * 60 * 60 * 1000;
    @Autowired
    @Qualifier("SilpoMarketApi")
    private MarketApi silpoMarketApi;
    @Autowired
    @Qualifier("ForaMarketApi")
    private MarketApi foraMarketApi;
    @Autowired
    private MarketImportService marketImportService;

    @Scheduled(fixedDelay = SIX_HOURS, initialDelay = SIX_HOURS)
    public void scheduleFixedDelayTask() {
        marketImportService.updatePrice(silpoMarketApi);
        marketImportService.updatePrice(foraMarketApi);
    }
}

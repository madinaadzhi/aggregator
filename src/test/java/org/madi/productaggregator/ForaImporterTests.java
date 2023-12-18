package org.madi.productaggregator;

import org.junit.jupiter.api.Test;
import org.madi.productaggregator.web.market.impl.fora.ForaMarketApi;
import org.madi.productaggregator.web.market.impl.silpo.SilpoMarketApi;
import org.madi.productaggregator.web.service.MarketImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ForaImporterTests {
    @Autowired
    private MarketImportService marketImportService;
    @Autowired
    private ForaMarketApi foraMarketApi;


    @Test
    void importMarket() {
        marketImportService.importMarket(foraMarketApi);
    }
}


package org.madi.productaggregator;

import org.junit.jupiter.api.Test;
import org.madi.productaggregator.web.dao.CategoryRepository;
import org.madi.productaggregator.web.entities.CategoryEntity;
import org.madi.productaggregator.web.market.impl.silpo.SilpoMarketApi;
import org.madi.productaggregator.web.service.MarketImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class SilpoImporterTests {
    @Autowired
    private MarketImportService marketImportService;
    @Test
    void importSilpoMarket() {
       marketImportService.importMarket(new SilpoMarketApi());
    }

}

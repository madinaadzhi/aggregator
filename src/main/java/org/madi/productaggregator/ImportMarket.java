package org.madi.productaggregator;

import lombok.extern.slf4j.Slf4j;
import org.madi.productaggregator.web.market.api.MarketApi;
import org.madi.productaggregator.web.service.MarketImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class ImportMarket implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MarketImportService marketImportService;

    public static void main(String[] args) {
        SpringApplication.run(ImportMarket.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length != 1 ) {
           log.info("Command have to have one argument: domain name of the market");
        } else {
            String marketToImport = args[0];
            List<MarketApi> marketApis = context.getBeansOfType(MarketApi.class).entrySet().stream()
                    .map(Map.Entry::getValue)
                    .filter(api -> api.getMarketInfo().getDomainName().equals(marketToImport))
                    .collect(Collectors.toList());

            if (marketApis.size() != 1) {
                log.info("Can not find API to import data for market " + marketToImport);
            } else {
                MarketApi marketApi = marketApis.get(0);
                log.info("Found {} API to import", marketApi.getMarketInfo().getName());
                marketImportService.importMarket(marketApi);
            }
        }
    }
}

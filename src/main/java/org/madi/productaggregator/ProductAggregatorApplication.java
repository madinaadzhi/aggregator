package org.madi.productaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProductAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductAggregatorApplication.class, args);
    }

}

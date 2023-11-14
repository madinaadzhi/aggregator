package org.madi.productaggregator;

import org.junit.jupiter.api.Test;
import org.madi.productaggregator.web.dao.AggregatorProductRepository;
import org.madi.productaggregator.web.entities.AggregatorProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class AggregatorProductTests {
    @Autowired
    private AggregatorProductRepository aggregatorProductRepository;
    @Test
    void loadProductById() {
        List<AggregatorProductEntity> aggregatorProductEntitiesByCategoryId = aggregatorProductRepository.findAggregatorProductEntitiesByCategoryId(26L);
        System.out.println(aggregatorProductEntitiesByCategoryId.size());
    }

}

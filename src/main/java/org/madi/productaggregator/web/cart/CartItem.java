package org.madi.productaggregator.web.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.madi.productaggregator.web.entities.AggregatorProductEntity;
import org.madi.productaggregator.web.entities.ProductEntity;

@Data
@AllArgsConstructor
public class CartItem {
    private AggregatorProductEntity aggregatorEntity;
    int quantity;

    public ProductEntity getProductEntity(Long marketId) {
        return aggregatorEntity.getProductEntities().stream()
                .filter(productEntity -> productEntity.getMarketEntity().getId().equals(marketId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "aggregator=" + (aggregatorEntity != null ? aggregatorEntity.getId() : "null") +
                ", quantity=" + quantity +
                '}';
    }
}

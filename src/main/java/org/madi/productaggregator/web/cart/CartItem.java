package org.madi.productaggregator.web.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.madi.productaggregator.web.entities.AggregatorProductEntity;

@Data
@AllArgsConstructor
public class CartItem {
    private AggregatorProductEntity aggregatorEntity;
    int quantity;

    @Override
    public String toString() {
        return "CartItem{" +
                "aggregator=" + (aggregatorEntity != null ? aggregatorEntity.getId() : "null") +
                ", quantity=" + quantity +
                '}';
    }
}
